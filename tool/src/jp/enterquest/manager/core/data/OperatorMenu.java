/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * 管理ツールオペレータ別メニュー情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class OperatorMenu
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
	private OperatorMenu()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "operator_menu";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * オペレータ別メニュー情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param menu_name メニュー名
		 * @return オペレータ別メニュー情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String operator_name, final String menu_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.MENU
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別メニュー情報を取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param menu_name メニュー名
		 * @return オペレータ別メニュー情報
		 */
		public final Row selectRow(final SqlConnection connection, final String operator_name, final String menu_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Column.OPERATOR
				, Column.MENU
				, Column.READABLE
				, Column.SELECTED
				, Table.NAME
				, Column.OPERATOR
				, Column.MENU
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, menu_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("opertor=%s menu=%s : row is not found in `%s`", operator_name, menu_name, Table.NAME));
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
		 * オペレータ別メニュー情報リストを取得する
		 * @param connection SQLコネクション
		 * @return オペレータ別メニュー情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.OPERATOR
				, Column.MENU
				, Column.READABLE
				, Column.SELECTED
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
		 * オペレータ別メニュー情報リストを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @return オペレータ別メニュー情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String operator_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? ORDER BY `%s` ASC"
				, Column.OPERATOR
				, Column.MENU
				, Column.READABLE
				, Column.SELECTED
				, Table.NAME
				, Column.OPERATOR
				, Column.MENU
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別メニュー情報を追加する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param menu_name メニュー名
		 */
		public final void insertRow(final SqlConnection connection, final String operator_name, final String menu_name)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.OPERATOR
				, Column.MENU
				, Column.READABLE
				, Column.SELECTED
				, Column.NOTES
				, Column.INSERT_TIME
				, Column.UPDATE_TIME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, menu_name);
				statement.setBoolean(index++, false);
				statement.setBoolean(index++, false);
				statement.setString(index++, "");
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別メニュー情報を更新する
		 * @param connection SQLコネクション
		 * @param operator_menu オペレータ別メニュー情報
		 */
		public final void updateRow(final SqlConnection connection, final Row operator_menu)
		{
			final String sql = String.format(
				"UPDATE `%s` SET `%s`=?,`%s`=NOW() WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.SELECTED
				, Column.UPDATE_TIME
				, Column.OPERATOR
				, Column.MENU
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setBoolean(index++, operator_menu.isSelected());
				statement.setString(index++, operator_menu.getOperator());
				statement.setString(index++, operator_menu.getMenu());
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別メニュー情報を削除する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param menu_name メニュー名
		 */
		public final void deleteRow(final SqlConnection connection, final String operator_name, final String menu_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.MENU
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, menu_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別メニュー情報が存在するかどうかを取得する
		 * @param operator_menus オペレータ別メニュー情報リスト
		 * @param operator_name オペレータ名
		 * @param menu_name メニュー名
		 * @return オペレータ別メニュー情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_menus, final String operator_name, final String menu_name)
		{
			for (final Row operator_menu : operator_menus)
			{
				if (operator_menu.getOperator().equals(operator_name)
					&& operator_menu.getMenu().equals(menu_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別メニュー情報が存在するかどうかを取得する
		 * @param operator_menus オペレータ別メニュー情報リスト
		 * @param menu_name メニュー名
		 * @return オペレータ別メニュー情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_menus, final String menu_name)
		{
			for (final Row operator_menu : operator_menus)
			{
				if (operator_menu.getMenu().equals(menu_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別メニュー情報を取得する
		 * @param operator_menus オペレータ別メニュー情報リスト
		 * @param menu_name メニュー名
		 * @return オペレータ別メニュー情報
		 */
		public final Row findRow(final Array<Row> operator_menus, final String menu_name)
		{
			for (final Row operator_menu : operator_menus)
			{
				if (operator_menu.getMenu().equals(menu_name))
				{
					return operator_menu;
				}
			}
			throw new RuntimeException(String.format("menu=%s : row is not found in `%s`", menu_name, Table.NAME));
		}

		/**
		 * 閲覧可能なメニューが存在するかどうかを取得する
		 * @param operator_menus オペレータ別メニュー情報リスト
		 * @return 閲覧可能なメニューが存在する場合はtrueを返す
		 */
		public final boolean isAnyReadable(final Array<Row> operator_menus)
		{
			for (final Row operator_menu : operator_menus)
			{
				if (operator_menu.isReadable())
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * 選択中のメニューが存在するかどうかを取得する
		 * @param operator_menus オペレータ別メニュー情報リスト
		 * @return 選択中のメニューが存在する場合はtrueを返す
		 */
		public final boolean isAnySelected(final Array<Row> operator_menus)
		{
			for (final Row operator_menu : operator_menus)
			{
				if (operator_menu.isSelected() && operator_menu.isReadable())
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
		/** オペレータ名 */
		private final String operator;
		/** メニュー名 */
		private final String menu;
		/** 閲覧可 */
		private final boolean readable;
		/** 選択中 */
		private volatile boolean selected;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.operator = result.getString(Column.OPERATOR);
			this.menu = result.getString(Column.MENU);
			this.readable = result.getBoolean(Column.READABLE);
			this.selected = result.getBoolean(Column.SELECTED);
		}

		/**
		 * オペレータ名を取得する
		 * @return オペレータ名
		 */
		public final String getOperator()
		{
			return this.operator;
		}

		/**
		 * メニュー名を取得する
		 * @return メニュー名
		 */
		public final String getMenu()
		{
			return this.menu;
		}

		/**
		 * 閲覧可能かどうかを取得する
		 * @return 閲覧可能な場合はtrueを返す
		 */
		public final boolean isReadable()
		{
			return this.readable;
		}

		/**
		 * 選択中かどうかを設定する
		 * @param selected 選択中の場合はtrueを指定する
		 */
		public final void isSelected(final boolean selected)
		{
			this.selected = selected;
		}

		/**
		 * 選択中かどうかを取得する
		 * @return 選択中の場合はtrueを返す
		 */
		public final boolean isSelected()
		{
			return this.selected;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** オペレータ名 */
		public static final String OPERATOR = "operator";
		/** メニュー名 */
		public static final String MENU = "menu";
		/** 閲覧可 */
		public static final String READABLE = "readable";
		/** 選択中 */
		public static final String SELECTED = "selected";
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
