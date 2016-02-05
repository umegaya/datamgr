/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * 管理ツールリレーション情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class Relation
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
	private Relation()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "relation";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * リレーション情報を追加する
		 * @param connection SQLコネクション
		 */
		public final void insertRow(final SqlConnection connection, final String database_name, 
			final String parent_table, final String parent_column, 
			final String child_table, final String child_column)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`='',`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.DATABASE
				, Column.PARENT_TABLE
				, Column.PARENT_COLUMN
				, Column.CHILD_TABLE
				, Column.CHILD_COLUMN
				, Column.NOTES
				, Column.INSERT_TIME
				, Column.UPDATE_TIME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, parent_table);
				statement.setString(index++, parent_column);
				statement.setString(index++, child_table);
				statement.setString(index++, child_column);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * リレーション情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param child_table_name 子テーブル名
		 * @param child_column_name 子カラム名
		 * @return リレーション情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String database_name, final String child_table_name, final String child_column_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.CHILD_TABLE
				, Column.CHILD_COLUMN
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, child_table_name);
				statement.setString(index++, child_column_name);
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
		 * リレーション情報リストを取得する
		 * @param connection SQLコネクション
		 * @return リレーション情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.DATABASE
				, Column.PARENT_TABLE
				, Column.PARENT_COLUMN
				, Column.CHILD_TABLE
				, Column.CHILD_COLUMN
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
		 * リレーション情報リストを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @return リレーション情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String database_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=?"
				, Column.DATABASE
				, Column.PARENT_TABLE
				, Column.PARENT_COLUMN
				, Column.CHILD_TABLE
				, Column.CHILD_COLUMN
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
		 * リレーション情報リストを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param child_table_name 子テーブル名
		 * @return リレーション情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String database_name, final String child_table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Column.DATABASE
				, Column.PARENT_TABLE
				, Column.PARENT_COLUMN
				, Column.CHILD_TABLE
				, Column.CHILD_COLUMN
				, Table.NAME
				, Column.DATABASE
				, Column.CHILD_TABLE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, child_table_name);
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
		 * リレーション情報を削除する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param child_table_name 子テーブル名
		 * @param child_column_name 子カラム名
		 */
		public final void deleteRowFromChild(
			final SqlConnection connection
			, final String database_name
			, final String child_table_name
			, final String child_column_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.CHILD_TABLE
				, Column.CHILD_COLUMN
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, child_table_name);
				statement.setString(index++, child_column_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * リレーション情報を削除する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param parent_table_name 親テーブル名
		 * @param parent_column_name 親カラム名
		 */
		public final void deleteRowsFromParent(
			final SqlConnection connection
			, final String database_name
			, final String parent_table_name
			, final String parent_column_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.PARENT_TABLE
				, Column.PARENT_COLUMN
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, parent_table_name);
				statement.setString(index++, parent_column_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * リレーション情報が存在するかどうかを取得する
		 * @param relations リレーション情報リスト
		 * @param child_table_name 子テーブル名
		 * @param child_column_name 子カラム名
		 * @return リレーション情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> relations, final String child_table_name, final String child_column_name)
		{
			for (final Row relation : relations)
			{
				if (relation.getChildTable().equals(child_table_name)
					&& relation.getChildColumn().equals(child_column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * リレーション情報を取得する
		 * @param relations リレーション情報リスト
		 * @param child_table_name 子テーブル名
		 * @param child_column_name 子カラム名
		 * @return リレーション情報
		 */
		public final Row findRow(final Array<Row> relations, final String child_table_name, final String child_column_name)
		{
			for (final Row relation : relations)
			{
				if (relation.getChildTable().equals(child_table_name)
					&& relation.getChildColumn().equals(child_column_name))
				{
					return relation;
				}
			}
			throw new RuntimeException(String.format("child_table=%s child_column=%s : row is not found in `%s`", child_table_name, child_column_name, Table.NAME));
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
		/** 親テーブル名 */
		private final String parent_table;
		/** 親カラム名 */
		private final String parent_column;
		/** 子テーブル名 */
		private final String child_table;
		/** 子カラム名 */
		private final String child_column;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.database = result.getString(Column.DATABASE);
			this.parent_table = result.getString(Column.PARENT_TABLE);
			this.parent_column = result.getString(Column.PARENT_COLUMN);
			this.child_table = result.getString(Column.CHILD_TABLE);
			this.child_column = result.getString(Column.CHILD_COLUMN);
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
		 * 親テーブル名を取得する
		 * @return 親テーブル名
		 */
		public final String getParentTable()
		{
			return this.parent_table;
		}

		/**
		 * 親カラム名を取得する
		 * @return 親カラム名
		 */
		public final String getParentColumn()
		{
			return this.parent_column;
		}

		/**
		 * 子テーブル名を取得する
		 * @return 子テーブル名
		 */
		public final String getChildTable()
		{
			return this.child_table;
		}

		/**
		 * 子カラム名を取得する
		 * @return 子カラム名
		 */
		public final String getChildColumn()
		{
			return this.child_column;
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
		/** 親テーブル名 */
		public static final String PARENT_TABLE = "parent_table";
		/** 親カラム名 */
		public static final String PARENT_COLUMN = "parent_column";
		/** 子テーブル名 */
		public static final String CHILD_TABLE = "child_table";
		/** 子カラム名 */
		public static final String CHILD_COLUMN = "child_column";
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
