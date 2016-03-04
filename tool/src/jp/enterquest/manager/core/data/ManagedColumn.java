/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;
import jp.enterquest.system.SqlOrder;

/**
 * 管理ツールカラム情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class ManagedColumn
{
	/**
	 * テーブル情報を取得する
	 * @return テーブル情報
	 */
	public static final Table getTable()
	{
		return Table.instance;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	private ManagedColumn()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "managed_column";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * カラム情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String database_name, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
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
		 * カラム情報を取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報
		 */
		public final Row selectRow(final SqlConnection connection, final String database_name, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
				, Column.ALIAS
				, Column.ORDINAL
				, Table.NAME
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				statement.setString(index++, column_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("database=%s table=%s column=%s : row is not found in `%s`", database_name, table_name, column_name, Table.NAME));
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
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
				, Column.ALIAS
				, Column.ORDINAL
				, Table.NAME
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
		 * @param database_name データベース名
		 * @return カラム情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String database_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=?"
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
				, Column.ALIAS
				, Column.ORDINAL
				, Table.NAME
				, Column.DATABASE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
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
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return カラム情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=? ORDER BY `%s` ASC"
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
				, Column.ALIAS
				, Column.ORDINAL
				, Table.NAME
				, Column.DATABASE
				, Column.TABLE
				, Column.ORDINAL
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
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
		 * カラム情報を追加する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @param ordinal カラム順序
		 */
		public final void insertRow(final SqlConnection connection, final String database_name, final String table_name, final String column_name, final int ordinal, final String note)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
				, Column.ALIAS
				, Column.ORDINAL
				, Column.NOTES
				, Column.INSERT_TIME
				, Column.UPDATE_TIME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				statement.setString(index++, column_name);
				statement.setString(index++, column_name);
				statement.setInt32(index++, ordinal);
				statement.setString(index++, note);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * カラム情報を更新する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @param ordinal カラム順序
		 */
		public final void updateRow(final SqlConnection connection, final String database_name, final String table_name, final String column_name, final int ordinal)
		{
			final String sql = String.format(
				"UPDATE `%s` SET `%s`=?,`%s`=NOW() WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.ORDINAL
				, Column.UPDATE_TIME
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setInt32(index++, ordinal);
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				statement.setString(index++, column_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * カラム情報を削除する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 */
		public final void deleteRow(final SqlConnection connection, final String database_name, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.TABLE
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				statement.setString(index++, column_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * カラム情報を存在するかどうかを取得する
		 * @param columns カラム情報リスト
		 * @param column_name カラム名
		 * @return カラム情報を存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> columns, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getName().equals(column_name))
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
				if (column.getTable().equals(table_name)
					&& column.getName().equals(column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * カラム情報が存在するかどうかを取得する
		 * @param columns カラム情報リスト
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> columns, final String database_name, final String table_name, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getDatabase().equals(database_name)
					&& column.getTable().equals(table_name)
					&& column.getName().equals(column_name))
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
				if (column.getName().equals(column_name))
				{
					return column;
				}
			}
			throw new RuntimeException(String.format("column=%s : row is not found in `%s`.", column_name, Table.NAME));
		}

		/**
		 * カラム情報を取得する
		 * @param columns カラム情報リスト
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報
		 */
		public final Row findRow(final Array<Row> columns, final String table_name, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getTable().equals(table_name)
					&& column.getName().equals(column_name))
				{
					return column;
				}
			}
			throw new RuntimeException(String.format("table=%s column=%s : row is not found in `%s`.", table_name, column_name, Table.NAME));
		}

		/**
		 * カラム情報を取得する
		 * @param columns カラム情報リスト
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return カラム情報
		 */
		public final Row findRow(final Array<Row> columns, final String database_name, final String table_name, final String column_name)
		{
			for (final Row column : columns)
			{
				if (column.getDatabase().equals(database_name)
					&& column.getTable().equals(table_name)
					&& column.getName().equals(column_name))
				{
					return column;
				}
			}
			throw new RuntimeException(String.format("database=%s table=%s column=%s : row is not found in `%s`.", database_name, table_name, column_name, Table.NAME));
		}
	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row
	{
		/** データベース名 */
		private final String database;
		/** テーブル名 */
		private final String table;
		/** カラム名 */
		private final String name;
		/** カラム別名 */
		private final String alias;
		/** カラム順序 */
		private final int ordinal;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.database = result.getString(Column.DATABASE);
			this.table = result.getString(Column.TABLE);
			this.name = result.getString(Column.NAME);
			this.alias = result.getString(Column.ALIAS);
			this.ordinal = result.getInt32(Column.ORDINAL);
		}

		public Row(final String table, final SqlResult result)
		{
			this.database = "game";
			this.table = table;
			this.name = result.getString("Field");
			this.alias = this.name;
			this.ordinal = 0;//(int)SqlOrder.ASC;
		}

		/**
		 * データベース名を取得する
		 * @return データベース名
		 */
		public final String getDatabase()
		{
			return this.database;
		}

		/**
		 * テーブル名を取得する
		 * @return テーブル名
		 */
		public final String getTable()
		{
			return this.table;
		}

		/**
		 * カラム名を取得する
		 * @return カラム名
		 */
		public final String getName()
		{
			return this.name;
		}

		/**
		 * カラム別名を取得する
		 * @return カラム別名
		 */
		public final String getAlias()
		{
			return this.alias;
		}

		/**
		 * カラム序数を取得する
		 * @return カラム序数
		 */
		public final int getOrdinal()
		{
			return this.ordinal;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** データベース名 */
		public static final String DATABASE = "database";
		/** テーブル名 */
		public static final String TABLE = "table";
		/** カラム名 */
		public static final String NAME = "name";
		/** カラム別名 */
		public static final String ALIAS = "alias";
		/** カラム順序 */
		public static final String ORDINAL = "ordinal";
		/** 備考 */
		public static final String NOTES = "notes";
		/** 作成時刻 */
		public static final String INSERT_TIME = "insert_time";
		/** 更新時刻 */
		public static final String UPDATE_TIME = "update_time";

		/**
		 * @deprecated
		 */
		@Deprecated
		private Column()
		{
		}
	}
}
