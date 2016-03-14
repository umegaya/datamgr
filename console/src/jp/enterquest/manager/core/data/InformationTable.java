/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.Hash;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * システムテーブル情報を提供するクラス
 * @author Akinori Nishimura
 */
public class InformationTable
{
	/**
	 * テーブル情報を取得する
	 * @return テーブル情報
	 */
	public static final Table getTable()
	{
		return Table.instance;
	}

	public static final Hash<String,Array<Row>> getCache()
	{
		return Table.cache;
	}

	public static final void setCache(Hash<String,Array<Row>> c) {
		Table.cache = c;
	}


	/**
	 * @deprecated
	 */
	@Deprecated
	private InformationTable()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** スキーマ名 */
		public static final String SCHEMA = "information_schema";
		/** テーブル名 */
		public static final String NAME = "TABLES";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/** キャッシュ */
		private static Hash<String,Array<Row>> cache = null;

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * テーブル情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param table_name テーブル名
		 * @return テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String table_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s`.`%s` WHERE `%s`=(SELECT DATABASE()) AND `%s`=?"
				, Table.SCHEMA
				, Table.NAME
				, Column.TABLE_SCHEMA
				, Column.TABLE_NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, table_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					return result.next();
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
		 * テーブル情報リストを取得する
		 * @param connection SQLコネクション
		 * @return テーブル情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			return this.selectRows(connection, null);
		}
		public final Array<Row> selectRows(final SqlConnection connection, final String sorted_by)
		{
			final String sql = String.format(
				"SELECT `%s` FROM `%s`.`%s` WHERE `%s`=(SELECT DATABASE()) %s"
				, Column.TABLE_NAME
				, Table.SCHEMA
				, Table.NAME
				, Column.TABLE_SCHEMA
				, (sorted_by == null ? "" : String.format("ORDER BY %s", sorted_by))
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				final SqlResult result = statement.executeQuery();
				try
				{
					final Array<Row> rows = Array.newInstance();
					while (result.next())
					{
						rows.add(new Row(result));
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

		/**
		 * テーブル情報が存在するかどうかを取得する
		 * @param information_tables テーブル情報リスト
		 * @param table_name テーブル名
		 * @return テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> information_tables, final String table_name)
		{
			for (final Row information_table : information_tables)
			{
				if (information_table.getTableName().equals(table_name))
				{
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row
	{
		/** テーブル名 */
		private final String table_name;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.table_name = result.getString(Column.TABLE_NAME);
		}

		/**
		 * テーブル名を取得する
		 * @return テーブル名
		 */
		public final String getTableName()
		{
			return this.table_name;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** スキーマ名 */
		public static final String TABLE_SCHEMA = "TABLE_SCHEMA";
		/** テーブル名 */
		public static final String TABLE_NAME = "TABLE_NAME";

		/**
		 * @deprecated
		 */
		@Deprecated
		private Column()
		{
		}
	}
}
