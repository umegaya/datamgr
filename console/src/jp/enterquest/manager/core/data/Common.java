/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.Data;
import jp.enterquest.system.DataFactory;
import jp.enterquest.system.Hash;
import jp.enterquest.system.Random;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlOrder;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;
import jp.enterquest.system.HttpServerRequest;

import jp.enterquest.system.ConsoleLogger;
import jp.enterquest.system.Logger;

import java.lang.reflect.*;
import java.sql.*;

/**
 * 管理ツール共通処理を提供するクラス
 * @author Akinori Nishimura
 */
public class Common
{
	/** シングルトンインスタンス */
	private static final Common instance = new Common();
	/**
	 * シングルトンインスタンスを取得する
	 * @return シングルトンインスタンス
	 */
	public static final Common getInstance()
	{
		return Common.instance;
	}

	/** 日付時刻フォーマット */
	private final java.text.DateFormat datetime_format;

	/**
	 * コンストラクタ
	 */
	private Common()
	{
		this.datetime_format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * レコード件数を取得する
	 * @param connection SQLコネクション
	 * @param table_name テーブル名
	 * @return レコード件数
	 */
	public final int selectCount(final SqlConnection connection, final String table_name)
	{
		final String sql = java.lang.String.format(
			"SELECT COUNT(*) FROM `%s`"
			, table_name
		);
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			final SqlResult result = statement.executeQuery();
			try
			{
				if (result.next())
				{
					return result.getInt32(0);
				}
				return 0;
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			statement.close();
		}
	}

	/* dirty hack for retrieving column names 
	because SqlResult encapsulate java.sql.ResultSet perfectly... */
	public ResultSetMetaData getResultMetaData(final SqlResult obj) {
		try {
			Field f = obj.getClass().getDeclaredField("source"); //NoSuchFieldException
			f.setAccessible(true);
			ResultSet rs = (ResultSet) f.get(obj); //IllegalAccessException
			return rs.getMetaData();
		}
		catch (Exception e) {
		}
		return null;
	}

	public Data selectRowsByStatement(final SqlConnection connection, final String sql) {
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			final SqlResult result = statement.executeQuery();
			final ResultSetMetaData meta = getResultMetaData(result);
			try
			{
				final DataFactory factory = DataFactory.getInstance();
				final Data rows = factory.newArray();
				final Data columns = factory.newArray();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					columns.asArray().add(factory.newString(meta.getColumnName(i)));
				}
				rows.asArray().add(columns);
				while (result.next()) {
					final Data row = factory.newArray();
					for (int i = 1; i <= meta.getColumnCount(); i++) {
						row.asArray().add(factory.newString(
							result.getString(
								meta.getColumnName(i)
							)
						));
					}
					rows.asArray().add(row);
				}
				return rows;
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			finally
			{
				result.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally
		{
			statement.close();
		}
	}

	/**
	 * レコード件数を取得する
	 * @param connection SQLコネクション
	 * @param table_name テーブル名
	 * @param columns カラム情報リスト
	 * @param conditions 条件ハッシュ
	 * @return レコード件数
	 */
	public final int selectCount(
		final SqlConnection connection
		, final String table_name
		, final Array<InformationColumn.Row> columns
		, final Hash<String,Data> conditions)
	{
		final StringBuilder buffer = new StringBuilder(1024);
		buffer.append("SELECT COUNT(*) FROM");
		buffer.append("`");
		buffer.append(table_name);
		buffer.append("`");

		if (!conditions.isEmpty())
		{
			boolean first = true;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final InformationColumn.Row column = InformationColumn.getTable().findRow(columns, column_name);
					final Data column_value = conditions.get(column_name);
					buffer.append(first ? " WHERE " : " AND ");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("`");
					if (column_value.isNull())
					{
						buffer.append(" IS NULL");
					}
					else if (column.isChar() || column.isVarchar() || column.isText())
					{
						buffer.append(" REGEXP ?");
					}
					else
					{
						buffer.append("=?");
					}
					first = false;
				}
			}
		}

		final String sql = buffer.toString();
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			int index  = 0;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final Data column_value = conditions.get(column_name);
					if (!column_value.isNull())
					{
						statement.setString(index++, column_value.asString());
					}
				}
			}
			final SqlResult result = statement.executeQuery();
			try
			{
				if (result.next())
				{
					return result.getInt32(0);
				}
				return 0;
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			statement.close();
		}
	}

	/**
	 * レコードを検索する
	 * @param connection SQLコネクション
	 * @param table_name テーブル名
	 * @param columns カラム情報リスト
	 * @param conditions 条件ハッシュ
	 * @param orders 順序ハッシュ
	 * @param limit リミット
	 * @param offset オフセット
	 * @return レコードリスト
	 */
	public final Data selectRows(
		final SqlConnection connection
		, final String table_name
		, final Array<InformationColumn.Row> columns
		, final Hash<String,Data> conditions
		, final Hash<String,SqlOrder> orders
		, final Integer limit
		, final Integer offset)
	{
		final StringBuilder buffer = new StringBuilder(1024);
		if (!columns.isEmpty())
		{
			boolean first = true;
			for (final InformationColumn.Row column : columns)
			{
				buffer.append(first ? "SELECT " : ",");
				buffer.append("`");
				buffer.append(column.getColumnName());
				buffer.append("`");
				first = false;
			}
			buffer.append(" FROM ");
			buffer.append("`");
			buffer.append(table_name);
			buffer.append("`");
		}
		if (!conditions.isEmpty())
		{
			boolean first = true;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final InformationColumn.Row column = InformationColumn.getTable().findRow(columns, column_name);
					final Data column_value = conditions.get(column_name);
					buffer.append(first ? " WHERE " : " AND ");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("`");
					if (column_value.isNull())
					{
						buffer.append(" IS NULL");
					}
					else if (column.isChar() || column.isVarchar() || column.isText())
					{
						buffer.append(" REGEXP ?");
					}
					else
					{
						buffer.append("=?");
					}
					first = false;
				}
			}
		}
		if (!orders.isEmpty())
		{
			boolean first = true;
			for (final String column_name : orders)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final SqlOrder order = orders.get(column_name);
					buffer.append(first ? " ORDER BY " : ",");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("` ");
					buffer.append(order.getName());
					first = false;
				}
			}
		}
		if (limit != null)
		{
			buffer.append(" LIMIT ");
			buffer.append(limit);
			if (offset != null)
			{
				buffer.append(" OFFSET ");
				buffer.append(offset);
			}
		}

		final String sql = buffer.toString();
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			int index  = 0;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final Data column_value = conditions.get(column_name);
					if (!column_value.isNull())
					{
						statement.setString(index++, column_value.asString());
					}
				}
			}
			final SqlResult result = statement.executeQuery();
			try
			{
				final DataFactory factory = DataFactory.getInstance();
				final Data rows = factory.newArray();
				while (result.next())
				{
					final Data row = factory.newHash();
					for (final InformationColumn.Row column : columns)
					{
						final String column_name = column.getColumnName();
						try {
							if (result.isNull(column_name))
							{
								row.asHash().set(column_name, factory.getNull());
							}
							else if (column.isDateTime() || column.isTimestamp())
							{
								//System.out.println(result.getDateTime(column_name));
								final String datetime = this.datetime_format.format(new java.util.Date(result.getDateTime(column_name)));
								row.asHash().set(column_name, factory.newString(datetime));
							}
							else
							{
								row.asHash().set(column_name, factory.newString(result.getString(column_name)));
							}
						}
						catch (Exception e) {
							row.asHash().set(column_name, factory.newString("data format error:" + e.getMessage()));
						}
					}
					rows.asArray().add(row);
				}
				return rows;
			}
			finally
			{
				result.close();
			}
		}
		finally
		{
			statement.close();
		}
	}

	public final boolean shouldOmitValue(final Array<InformationColumn.Row> columns, final Hash<String,Data> values, final String column_name) {
		InformationColumn.Row row = InformationColumn.getTable().findRow(columns, column_name);
		//auto increment であって、値が設定されていない、あるいはemptyの場合.値を省略することで、autoincrementに任せる.
		return (row != null && row.isAutoIncrement()) && (
			!values.has(column_name) || values.get(column_name).isNull() || 
			(values.get(column_name).isInt32() && values.get(column_name).asInt32() == 0)
		);
	}

	/**
	 * レコードを追加する
	 * @param connection SQLコネクション
	 * @param table テーブル情報
	 * @param columns カラム情報リスト
	 * @param values 値ハッシュ
	 * @return 追加した件数
	 */
	public final int insertRow(
		final SqlConnection connection
		, final ManagedTable.Row table
		, final Array<InformationColumn.Row> columns
		, final Hash<String,Data> values
		, final Logger logger)
	{
		final StringBuilder buffer = new StringBuilder(1024);
		buffer.append("INSERT INTO ");
		buffer.append("`");
		buffer.append(table.getName());
		buffer.append("`");

		if (!values.isEmpty())
		{
			boolean first = true;
			for (final String column_name : values)
			{
				if (!this.shouldOmitValue(columns, values, column_name))
				{
					final Data value = values.get(column_name);
					buffer.append(first ? " SET " : ",");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("`");
					buffer.append(value.isNull() ? "=NULL" : "=?");
					first = false;
				}
			}
			final String insert_time_column = table.getInsertTimeColumn();
			if (!values.has(insert_time_column))
			{
				if (InformationColumn.getTable().hasRow(columns, insert_time_column))
				{
					buffer.append(first ? " SET " : ",");
					buffer.append("`");
					buffer.append(insert_time_column);
					buffer.append("`");
					buffer.append("=NOW()");
				}
			}
			final String update_time_column = table.getUpdateTimeColumn();
			if (!values.has(update_time_column))
			{
				if (InformationColumn.getTable().hasRow(columns, update_time_column))
				{
					buffer.append(first ? " SET " : ",");
					buffer.append("`");
					buffer.append(update_time_column);
					buffer.append("`");
					buffer.append("=NOW()");
				}
			}
		}

		final String sql = buffer.toString();
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			int index = 0;
			for (final String column_name : values)
			{
				if (!this.shouldOmitValue(columns, values, column_name))
				{
					final Data column_value = values.get(column_name);
					if (!column_value.isNull())
					{
						statement.setString(index++, column_value.asString());
					}
				}
			}
			return statement.executeUpdate();
		}
		finally
		{
			statement.close();
		}
	}

	/**
	 * レコードを更新する
	 * @param connection SQLコネクション
	 * @param table テーブル情報
	 * @param columns カラム情報リスト
	 * @param values 値ハッシュ
	 * @param conditions 条件ハッシュ
	 * @return 更新した件数
	 */
	public final int updateRow(
		final SqlConnection connection
		, final ManagedTable.Row table
		, final Array<InformationColumn.Row> columns
		, final Hash<String,Data> values
		, final Hash<String,Data> conditions)
	{
		final StringBuilder buffer = new StringBuilder(1024);
		buffer.append("UPDATE ");
		buffer.append("`");
		buffer.append(table.getName());
		buffer.append("`");

		if (!values.isEmpty())
		{
			boolean first = true;
			for (final String column_name : values)
			{
				if (!this.shouldOmitValue(columns, values, column_name))
				{
					final Data value = values.get(column_name);
					buffer.append(first ? " SET " : ",");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("`");
					buffer.append(value.isNull() ? "=NULL" : "=?");
					first = false;
				}
			}
			final String update_time_column = table.getUpdateTimeColumn();
			if (!values.has(update_time_column))
			{
				if (InformationColumn.getTable().hasRow(columns, update_time_column))
				{
					buffer.append(first ? " SET " : ",");
					buffer.append("`");
					buffer.append(update_time_column);
					buffer.append("`");
					buffer.append("=NOW()");
				}
			}
		}
		if (!conditions.isEmpty())
		{
			boolean first = true;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final Data value = conditions.get(column_name);
					buffer.append(first ? " WHERE " : " AND ");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("`");
					buffer.append(value.isNull() ? " IS NULL" : "=?");
					first = false;
				}
			}
		}

		final String sql = buffer.toString();
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			int index = 0;
			for (final String column_name : values)
			{
				if (!this.shouldOmitValue(columns, values, column_name))
				{
					final Data column_value = values.get(column_name);
					if (!column_value.isNull())
					{
						statement.setString(index++, column_value.asString());
					}
				}
			}
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final Data column_value = conditions.get(column_name);
					if (!column_value.isNull())
					{
						statement.setString(index++, column_value.asString());
					}
				}
			}
			return statement.executeUpdate();
		}
		finally
		{
			statement.close();
		}
	}

	/**
	 * レコードを削除する
	 * @param connection SQLコネクション
	 * @param table_name テーブル名
	 * @param columns カラム情報リスト
	 * @param conditions 条件ハッシュ
	 * @return 削除した件数
	 */
	public final int deleteRow(
		final SqlConnection connection
		, final String table_name
		, final Array<InformationColumn.Row> columns
		, final Hash<String,Data> conditions)
	{
		final StringBuilder buffer = new StringBuilder(1024);
		buffer.append("DELETE FROM ");
		buffer.append("`");
		buffer.append(table_name);
		buffer.append("`");

		if (!conditions.isEmpty())
		{
			boolean first = true;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final Data column_value = conditions.get(column_name);
					buffer.append(first ? " WHERE " : " AND ");
					buffer.append("`");
					buffer.append(column_name);
					buffer.append("`");
					buffer.append(column_value.isNull() ? " IS NULL" : "=?");
					first = false;
				}
			}
		}

		final String sql = buffer.toString();
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			int index = 0;
			for (final String column_name : conditions)
			{
				if (InformationColumn.getTable().hasRow(columns, column_name))
				{
					final Data column_value = conditions.get(column_name);
					if (!column_value.isNull())
					{
						statement.setString(index++, column_value.asString());
					}
				}
			}
			return statement.executeUpdate();
		}
		finally
		{
			statement.close();
		}
	}

	/**
	 * レコードを削除する
	 * @param connection SQLコネクション
	 * @param table_name テーブル名
	 * @return 削除した件数
	 */
	public final int deleteRows(final SqlConnection connection, final String table_name)
	{
		final String sql = String.format(
			"DELETE FROM `%s`"
			, table_name
		);
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			return statement.executeUpdate();
		}
		finally
		{
			statement.close();
		}
	}

	/**
	 * 空白行の値を取得する
	 * @param column カラム情報
	 * @return 空白行の値
	 */
	public final Data getEmptyValue(final InformationColumn.Row column)
	{
		final DataFactory factory = DataFactory.getInstance();
		if (!column.isNullable())
		{
			if (column.isTinyInt()
				|| column.isSmallInt()
				|| column.isMediumInt()
				|| column.isInt()
				|| column.isBigInt()
				|| column.isFloat()
				|| column.isDouble())
			{
				return factory.newInt32(0);
			}
			else if (column.isDate())
			{
				return factory.newString("2000-01-01");
			}
			else if (column.isTime())
			{
				return factory.newString("00:00:00");
			}
			else if (column.isDateTime() || column.isTimestamp())
			{
				return factory.newString("2000-01-01 00:00:00");
			}
			else if (column.isChar() || column.isVarchar() || column.isText() || column.isBlob())
			{
				return factory.newString("");
			}
		}
		return factory.getNull();
	}

	/**
	 * ランダムな値を取得する
	 * @param connection SQLコネクション
	 * @param table テーブル情報
	 * @param columns カラム情報リスト
	 * @return ランダムな値
	 */
	public final Data getRandomValue(final SqlConnection connection, final ManagedTable.Row table, final Array<InformationColumn.Row> columns)
	{
		final Random random = Random.newInstance();
		final Hash<String,Data> conditions = Hash.newInstance();
		final DataFactory factory = DataFactory.getInstance();
		for (int loop = 0; loop < 100; loop++)
		{
			final Data value = factory.newInt32(random.nextInt32() % 10000000 + 10000000);
			conditions.set(table.getAutoRandomColumn(), value);
			if (this.selectCount(connection, table.getName(), columns, conditions) == 0)
			{
				return value;
			}
		}
		throw new RuntimeException("random value is not generated.");
	}

	public final int writeBySql(final SqlConnection connection, final String sql) {
		final SqlStatement statement = connection.newStatement(sql);
		try
		{
			return statement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		finally
		{
			statement.close();
		}
	}

	public static void setUrlAttribute(final HttpServerRequest request, final Logger logger) {
		try {
			String url = request.getRequestUrl();
			if (request.hasHeader("X-Forwarded-Proto")) {
				final String fwdproto = (request.getHeader("X-Forwarded-Proto").asString());
				url = url.replaceFirst("^[\\w]+://", fwdproto + "://");
			}
			request.setAttribute("url", url);
			request.setAttribute("baseUrl", url.replaceFirst("/[\\w]+$", ""));
			//logger.info("urls:%s %s %s", request.getAttribute("url"), request.getAttribute("baseUrl"), request.getHeader("X-Forwarded-Proto").asString());
		}
		catch (Exception e) {
			logger.info("error setUrlAttribute: " + e.getMessage());
		}
	}
}
