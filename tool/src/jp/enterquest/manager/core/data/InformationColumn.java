/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.Hash;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlDataType;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * システムカラム情報を提供するクラス
 * @author Akinori Nishimura
 */
public class InformationColumn
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
	private InformationColumn()
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
		public static final String NAME = "COLUMNS";

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
		 * カラム情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s`.`%s` WHERE `%s`=(SELECT DATABASE()) AND `%s`=? AND `%s`=?"
				, Table.SCHEMA
				, Table.NAME
				, Column.TABLE_SCHEMA
				, Column.TABLE_NAME
				, Column.COLUMN_NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, table_name);
				statement.setString(index++, column_name);
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
		 * カラム情報リストを取得する
		 * @param connection SQLコネクション
		 * @return カラム情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`.`%s` WHERE `%s`=(SELECT DATABASE())"
				, Column.TABLE_NAME
				, Column.COLUMN_NAME
				, Column.ORDINAL_POSITION
				, Column.IS_NULLABLE
				, Column.DATA_TYPE
				, Column.COLLATION_NAME
				, Column.COLUMN_TYPE
				, Column.EXTRA
				, Column.COLUMN_DEFAULT
				, Table.SCHEMA
				, Table.NAME
				, Column.TABLE_SCHEMA
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
		 * カラム情報リストを取得する
		 * @param connection SQLコネクション
		 * @param table_name テーブル名
		 * @return カラム情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`.`%s` WHERE `%s`=(SELECT DATABASE()) AND `%s`=? ORDER BY `%s` ASC"
				, Column.TABLE_NAME
				, Column.COLUMN_NAME
				, Column.ORDINAL_POSITION
				, Column.IS_NULLABLE
				, Column.DATA_TYPE
				, Column.COLLATION_NAME
				, Column.COLUMN_TYPE
				, Column.EXTRA
				, Column.COLUMN_DEFAULT
				, Table.SCHEMA
				, Table.NAME
				, Column.TABLE_SCHEMA
				, Column.TABLE_NAME
				, Column.ORDINAL_POSITION
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, table_name);
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
		 * カラム情報が存在するかどうかを取得する
		 * @param columns カラム情報リスト
		 * @param column_name カラム名
		 * @return カラム情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> columns, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getColumnName().equals(column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * カラム情報が存在するかどうかを取得する
		 * @param columns カラム情報リスト
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> columns, final String table_name, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getTableName().equals(table_name)
					&& column.getColumnName().equals(column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * カラム情報を取得する
		 * @param columns カラム情報リスト
		 * @param column_name カラム名
		 * @return カラム情報
		 */
		public final Row findRow(final Array<Row> columns, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getColumnName().equals(column_name))
				{
					return column;
				}
			}
			return null;
		}

		/**
		 * カラム情報が存在するかどうかを取得する
		 * @param columns カラム情報リスト
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報が存在する場合はtrueを返す
		 */
		public final Row findRow(final Array<Row> columns, final String table_name, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getTableName().equals(table_name)
					&& column.getColumnName().equals(column_name))
				{
					return column;
				}
			}
			return null;
		}

	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row extends Object
	{
		/** YES */
		private static final String YES = "YES";
		/** 自動採番 */
		private static final String AUTO_INCREMENT = "auto_increment";

		/** テーブル名 */
		private final String table_name;
		/** カラム名 */
		private final String column_name;
		/** カラム順序 */
		private final int ordinal;
		/** null可否 */
		private final boolean nullable;
		/** データタイプ名 */
		private final String data_type;
		/** 参照名 */
		private final String collation_name;
		/** カラムタイプ名 */
		private final String column_type;
		/** 自動 */
		private final boolean auto_increment;
		/** デフォルト値 */
		private final String column_default;
		/** コメント */
		private final String column_comment;
		

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.table_name = result.getString(Column.TABLE_NAME);
			this.column_name = result.getString(Column.COLUMN_NAME);
			this.ordinal = result.getInt32(Column.ORDINAL_POSITION);
			this.nullable = result.getString(Column.IS_NULLABLE).equals(Row.YES);
			this.data_type = result.getString(Column.DATA_TYPE);
			this.collation_name = result.getString(Column.COLLATION_NAME);
			this.column_type = result.getString(Column.COLUMN_TYPE);
			this.column_default = result.getString(Column.COLUMN_DEFAULT);
			this.auto_increment = result.getString(Column.EXTRA).equals(Row.AUTO_INCREMENT);
			this.column_comment = result.getString(Column.COLUMN_COMMENT);
		}

		/**
		 * テーブル名を取得する
		 * @return テーブル名
		 */
		public final String getTableName()
		{
			return this.table_name;
		}

		/**
		 * カラム名を取得する
		 * @return カラム名
		 */
		public final String getColumnName()
		{
			return this.column_name;
		}

		/**
		 * カラム順序を取得する
		 * @return カラム順序
		 */
		public final int getOrdinal()
		{
			return this.ordinal;
		}

		/**
		 * nullを許可するかどうかを取得する
		 * @return nullを許可する場合はtrueを返す
		 */
		public final boolean isNullable()
		{
			return this.nullable;
		}

		/**
		 * データタイプ名を取得する
		 * @return データタイプ名
		 */
		public final String getDataType()
		{
			return this.data_type;
		}

		/**
		 * データタイプがTINYINT型かどうかを取得する
		 * @return データタイプがTINYINT型の場合はtrueを返す
		 */
		public final boolean isTinyInt()
		{
			return SqlDataType.TINYINT.equals(this.data_type);
		}

		/**
		 * データタイプがSMALLINT型かどうかを取得する
		 * @return データタイプがSMALLINT型の場合はtrueを返す
		 */
		public final boolean isSmallInt()
		{
			return SqlDataType.SMALLINT.equals(this.data_type);
		}

		/**
		 * データタイプがMEDIUMINT型かどうかを取得する
		 * @return データタイプがMEDIUMINT型の場合はtrueを返す
		 */
		public final boolean isMediumInt()
		{
			return SqlDataType.MEDIUMINT.equals(this.data_type);
		}

		/**
		 * データタイプがINT型かどうかを取得する
		 * @return データタイプがINT型の場合はtrueを返す
		 */
		public final boolean isInt()
		{
			return SqlDataType.INT.equals(this.data_type);
		}

		/**
		 * データタイプがBIGINT型かどうかを取得する
		 * @return データタイプがBIGINT型の場合はtrueを返す
		 */
		public final boolean isBigInt()
		{
			return SqlDataType.BIGINT.equals(this.data_type);
		}

		/**
		 * データタイプがFLOAT型かどうかを取得する
		 * @return データタイプがFLOAT型の場合はtrueを返す
		 */
		public final boolean isFloat()
		{
			return SqlDataType.FLOAT.equals(this.data_type);
		}

		/**
		 * データタイプがDOUBLE型かどうかを取得する
		 * @return データタイプがDOUBLE型の場合はtrueを返す
		 */
		public final boolean isDouble()
		{
			return SqlDataType.DOUBLE.equals(this.data_type);
		}

		/**
		 * データタイプがDATE型かどうかを取得する
		 * @return データタイプがDATE型の場合はtrueを返す
		 */
		public final boolean isDate()
		{
			return SqlDataType.DATE.equals(this.data_type);
		}

		/**
		 * データタイプがTIME型かどうかを取得する
		 * @return データタイプがTIME型の場合はtrueを返す
		 */
		public final boolean isTime()
		{
			return SqlDataType.TIME.equals(this.data_type);
		}

		/**
		 * データタイプがDATETIME型かどうかを取得する
		 * @return データタイプがDATETIME型の場合はtrueを返す
		 */
		public final boolean isDateTime()
		{
			return SqlDataType.DATETIME.equals(this.data_type);
		}

		/**
		 * データタイプがTIMESTAMP型かどうかを取得する
		 * @return データタイプがTIMESTAMP型の場合はtrueを返す
		 */
		public final boolean isTimestamp()
		{
			return SqlDataType.TIMESTAMP.equals(this.data_type);
		}

		/**
		 * データタイプがCHAR型かどうかを取得する
		 * @return データタイプがCHAR型の場合はtrueを返す
		 */
		public final boolean isChar()
		{
			return SqlDataType.CHAR.equals(this.data_type);
		}

		/**
		 * データタイプがVARCHAR型かどうかを取得する
		 * @return データタイプがVARCHAR型の場合はtrueを返す
		 */
		public final boolean isVarchar()
		{
			return SqlDataType.VARCHAR.equals(this.data_type);
		}

		/**
		 * データタイプがTEXT型かどうかを取得する
		 * @return データタイプがTEXT型の場合はtrueを返す
		 */
		public final boolean isText()
		{
			return SqlDataType.TEXT.equals(this.data_type);
		}

		/**
		 * 照合名を取得する
		 * @return 照合名
		 */
		public final String getCollationName()
		{
			return this.collation_name;
		}

		/**
		 * カラムタイプ名を取得する
		 * @return カラムタイプ名
		 */
		public final String getColumnType()
		{
			return this.column_type;
		}
		/**
		 * カラムのデフォルト値を取得する
		 * @return カラムのデフォルト値の文字列表現	
		 */
		public final String getColumnDefault()
		{
			return this.column_default;
		}

		/**
		 * 自動採番が有効かどうかを取得する
		 * @return 自動採番が有効な場合はtrueを返す
		 */
		public final boolean isAutoIncrement()
		{
			return this.auto_increment;
		}

		/**
		 * コメントを取得
		 */
		public final String getComment() {
			return this.column_comment;
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
		/** カラム名 */
		public static final String COLUMN_NAME = "COLUMN_NAME";
		/** カラム順序 */
		public static final String ORDINAL_POSITION = "ORDINAL_POSITION";
		/** null許可/不可 */
		public static final String IS_NULLABLE = "IS_NULLABLE";
		/** データタイプ名 */
		public static final String DATA_TYPE = "DATA_TYPE";
		/** 照合名 */
		public static final String COLLATION_NAME = "COLLATION_NAME";
		/** カラムタイプ名 */
		public static final String COLUMN_TYPE = "COLUMN_TYPE";
		/** エクストラ */
		public static final String EXTRA = "EXTRA";
		/** デフォルト値 */
		public static final String COLUMN_DEFAULT = "COLUMN_DEFAULT";
		/** コメント値 */
		public static final String COLUMN_COMMENT = "COLUMN_COMMENT";

		/**
		 * @deprecated
		 */
		@Deprecated
		private Column()
		{
		}
	}
}
