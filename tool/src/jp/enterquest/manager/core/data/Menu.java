/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * 管理ツールメニュー情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class Menu
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
	private Menu()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "menu";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * メニュー情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param menu_name メニュー名
		 * @return メニュー情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String menu_name)
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
				statement.setString(index++, menu_name);
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
		 * メニュー情報リストを取得する
		 * @param connection SQLコネクション
		 * @return メニュー情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s` FROM `%s`"
				, Column.NAME
				, Column.ALIAS
				, Column.PATH
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
		 * メニュー情報が存在するかどうかを取得する
		 * @param menus メニュー情報リスト
		 * @param menu_name メニュー名
		 * @return メニュー情報が存在するかどうか
		 */
		public final boolean hasRow(final Array<Row> menus, final String menu_name)
		{
			for (final Row menu : menus)
			{
				if (menu.getName().equals(menu_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * メニュー情報を取得する
		 * @param menus メニュー情報リスト
		 * @param menu_name メニュー名
		 * @return メニュー情報
		 */
		public final Row findRow(final Array<Row> menus, final String menu_name)
		{
			for (final Row menu : menus)
			{
				if (menu.getName().equals(menu_name))
				{
					return menu;
				}
			}
			throw new RuntimeException(String.format("menu=%s : row is not found in `%s`.", menu_name, Table.NAME));
		}
	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row
	{
		/** メニュー名 */
		private final String name;
		/** メニュー別名 */
		private final String alias;
		/** パス名 */
		private final String path;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.name = result.getString(Column.NAME);
			this.alias = result.getString(Column.ALIAS);
			this.path = result.getString(Column.PATH);
		}

		/**
		 * メニュー名を取得する
		 * @return メニュー名
		 */
		public final String getName()
		{
			return this.name;
		}

		/**
		 * メニュー別名を取得する
		 * @return メニュー別名
		 */
		public final String getAlias()
		{
			return this.alias;
		}

		/**
		 * パス名を取得する
		 * @return パス名
		 */
		public final String getPath()
		{
			return this.path;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** メニュー名 */
		public static final String NAME = "name";
		/** メニュー別名 */
		public static final String ALIAS = "alias";
		/** パス名 */
		public static final String PATH = "path";
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
