/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * 管理ツールデータベース情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class ManagedDatabase
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
	private ManagedDatabase()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "managed_database";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * データベース情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @return データベース情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String database_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=?"
				, Table.NAME
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
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
		 * データベース情報を取得する
		 * @param connection SQLコネクション
		 * @param database_name データベース名
		 * @return データベース情報
		 */
		public final Row selectRow(final SqlConnection connection, final String database_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s` FROM `%s` WHERE `%s`=?"
				, Column.NAME
				, Column.ALIAS
				, Column.RESOURCE
				, Table.NAME
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, database_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("database=%s : row is not found in `%s`.", database_name, Table.NAME));
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
		 * データベース情報リストを取得する
		 * @param connection SQLコネクション
		 * @return データベース情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s` FROM `%s`"
				, Column.NAME
				, Column.ALIAS
				, Column.RESOURCE
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
		 * データベース情報が存在するかどうかを取得する
		 * @param databases データベース情報リスト
		 * @param database_name データベース名
		 * @return データベース情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> databases, final String database_name)
		{
			for (final Row database : databases)
			{
				if (database.getName().equals(database_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * データベース情報を取得する
		 * @param databases データベース情報リスト
		 * @param database_name データベース名
		 * @return データベース情報
		 */
		public final Row findRow(final Array<Row> databases, final String database_name)
		{
			for (final Row database : databases)
			{
				if (database.getName().equals(database_name))
				{
					return database;
				}
			}
			throw new RuntimeException(String.format("database=%s : row is not found in `%s`.", database_name, Table.NAME));
		}
	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row
	{
		/** データベース名 */
		private final String name;
		/** データベース別名 */
		private final String alias;
		/** リソース名 */
		private final String resource;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.name = result.getString(Column.NAME);
			this.alias = result.getString(Column.ALIAS);
			this.resource = result.getString(Column.RESOURCE);
		}

		/**
		 * データベース名を取得する
		 * @return データベース名
		 */
		public final String getName()
		{
			return this.name;
		}

		/**
		 * データベース別名を取得する
		 * @return データベース別名
		 */
		public final String getAlias()
		{
			return this.alias;
		}

		/**
		 * リソース名を取得する
		 * @return リソース名
		 */
		public final String getResource()
		{
			return this.resource;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** データベース名 */
		public static final String NAME = "name";
		/** データベース別名 */
		public static final String ALIAS = "alias";
		/** リソース名 */
		public static final String RESOURCE = "resource";
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
