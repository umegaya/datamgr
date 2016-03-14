/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.Hash;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

import jp.enterquest.manager.core.data.Relation;
import jp.enterquest.manager.core.data.InformationColumn;

/**
 * 管理ツールテーブル情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class ManagedTable
{
	/**
	 * テーブル情報を取得する
	 * @return テーブル情報
	 */
	public static final Table getTable()
	{
		return Table.instance;
	}

	public static final Array<Row> getCache()
	{
		return Table.cache;
	}
	public static final void setCache(Array<Row> c) {
		Table.cache = c;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	private ManagedTable()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "managed_table";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/** キャッシュ */
		private static Array<Row> cache = null;

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * テーブル情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.NAME
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
		 * テーブル情報を取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return テーブル情報
		 */
		public final Row selectRow(final SqlConnection connection, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Column.DATABASE
				, Column.NAME
				, Column.ALIAS
				, Column.DISPLAY_COLUMN
				, Column.AUTO_RANDOM_COLUMN
				, Column.RENUMBERING_COLUMN
				, Column.INSERT_TIME_COLUMN
				, Column.UPDATE_TIME_COLUMN
				, Table.NAME
				, Column.DATABASE
				, Column.NAME
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
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("table=%s : row is not found in `%s`.", table_name, Table.NAME));
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
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.DATABASE
				, Column.NAME
				, Column.ALIAS
				, Column.DISPLAY_COLUMN
				, Column.AUTO_RANDOM_COLUMN
				, Column.RENUMBERING_COLUMN
				, Column.INSERT_TIME_COLUMN
				, Column.UPDATE_TIME_COLUMN
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
		 * テーブル情報リストを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @return テーブル情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String database_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=?"
				, Column.DATABASE
				, Column.NAME
				, Column.ALIAS
				, Column.DISPLAY_COLUMN
				, Column.AUTO_RANDOM_COLUMN
				, Column.RENUMBERING_COLUMN
				, Column.INSERT_TIME_COLUMN
				, Column.UPDATE_TIME_COLUMN
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
		 * テーブル情報を追加する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 */
		public final void insertRow(final SqlConnection connection, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.DATABASE
				, Column.NAME
				, Column.ALIAS
				, Column.DISPLAY_COLUMN
				, Column.AUTO_RANDOM_COLUMN
				, Column.RENUMBERING_COLUMN
				, Column.INSERT_TIME_COLUMN
				, Column.UPDATE_TIME_COLUMN
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
				statement.setString(index++, table_name);
				statement.setString(index++, "name");
				statement.setString(index++, "");
				statement.setString(index++, "sort");
				statement.setString(index++, "insert_time");
				statement.setString(index++, "update_time");
				statement.setString(index++, "");
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * テーブル情報を削除する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 */
		public final void deleteRow(final SqlConnection connection, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.DATABASE
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * テーブル情報が存在するかどうかを取得する
		 * @param tables テーブル情報リスト
		 * @param table_name テーブル名
		 * @return テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> tables, final String table_name)
		{
			for (final Row table : tables)
			{
				if (table.getName().equals(table_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * テーブル情報が存在するかどうかを取得する
		 * @param tables テーブル情報リスト
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> tables, final String database_name, final String table_name)
		{
			for (final Row table : tables)
			{
				if (table.getDatabase().equals(database_name) && table.getName().equals(table_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * テーブル情報を取得する
		 * @param tables テーブル情報リスト
		 * @param table_name テーブル名
		 * @return テーブル情報
		 */
		public final Row findRow(final Array<Row> tables, final String table_name)
		{
			for (final Row table : tables)
			{
				if (table.getName().equals(table_name))
				{
					return table;
				}
			}
			throw new RuntimeException(String.format("table=%s : row is not found in `%s`.", table_name, Table.NAME));
		}

		/**
		 * リレーションが表示可能か調べる
		 */
		public final boolean isRenderableRelation(final Array<Relation.Row> relations, 
			final String child_table, final String child_column) {
			try {
				//relationが存在して、display_columnがInformationColumnに存在する.
				Relation.Row relation = Relation.getTable().findRow(relations, child_table, child_column);
				Array<InformationColumn.Row> columns = InformationColumn.getCache().get(relation.getDatabase());
				Array<Row> managed_tables = ManagedTable.getCache();
				Row managed_table = this.findRow(managed_tables, relation.getParentTable());
				if (InformationColumn.getTable().hasRow(columns, relation.getParentTable(), managed_table.getDisplayColumn())) {
					return true;
				}
			}
			catch (Exception e) {
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
		/** データベース名 */
		private final String database;
		/** テーブル名 */
		private final String name;
		/** テーブル別名 */
		private final String alias;
		/** 表示カラム名 */
		private final String display_column;
		/** オートランダムカラム名 */
		private final String auto_random_column;
		/** リナンバリングカラム名 */
		private final String renumbering_column;
		/** 作成時刻カラム名 */
		private final String insert_time_column;
		/** 更新時刻カラム名 */
		private final String update_time_column;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.database = result.getString(Column.DATABASE);
			this.name = result.getString(Column.NAME);
			this.alias = result.getString(Column.ALIAS);
			this.display_column = result.getString(Column.DISPLAY_COLUMN);
			this.auto_random_column = result.getString(Column.AUTO_RANDOM_COLUMN);
			this.renumbering_column = result.getString(Column.RENUMBERING_COLUMN);
			this.insert_time_column = result.getString(Column.INSERT_TIME_COLUMN);
			this.update_time_column = result.getString(Column.UPDATE_TIME_COLUMN);
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
		public final String getName()
		{
			return this.name;
		}

		/**
		 * テーブル別名を取得する
		 * @return テーブル別名
		 */
		public final String getAlias()
		{
			return this.alias;
		}

		/**
		 * 表示カラム名を取得する
		 * @return 表示カラム名
		 */
		public final String getDisplayColumn()
		{
			return this.display_column;
		}

		/**
		 * オートランダムカラム名を取得する
		 * @return オートランダムカラム名
		 */
		public final String getAutoRandomColumn()
		{
			return this.auto_random_column;
		}

		/**
		 * リナンバリングカラム名を取得する
		 * @return リナンバリングカラム名
		 */
		public final String getRenumberingColumn()
		{
			return this.renumbering_column;
		}

		/**
		 * 作成時刻カラム名を取得する
		 * @return 作成時刻カラム名
		 */
		public final String getInsertTimeColumn()
		{
			return this.insert_time_column;
		}

		/**
		 * 更新時刻カラム名を取得する
		 * @return 更新時刻カラム名
		 */
		public final String getUpdateTimeColumn()
		{
			return this.update_time_column;
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
		public static final String NAME = "name";
		/** テーブル別名 */
		public static final String ALIAS = "alias";
		/** 表示カラム名 */
		public static final String DISPLAY_COLUMN = "display_column";
		/** オートランダムカラム名 */
		public static final String AUTO_RANDOM_COLUMN = "auto_random_column";
		/** リナンバリングカラム名 */
		public static final String RENUMBERING_COLUMN = "renumbering_column";
		/** 作成時刻カラム名 */
		public static final String INSERT_TIME_COLUMN = "insert_time_column";
		/** 更新時刻カラム名 */
		public static final String UPDATE_TIME_COLUMN = "update_time_column";
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
