/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * 管理ツールオペレータ情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class Operator
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
	private Operator()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "operator";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * オペレータ情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @return オペレータ情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String operator_name)
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
				statement.setString(index++, operator_name);
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
		 * オペレータ情報を取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @return オペレータ情報
		 */
		public final Row selectRow(final SqlConnection connection, final String operator_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s` FROM `%s` WHERE `%s`=?"
				, Column.NAME
				, Column.PASSWORD
				, Column.PUBLISHED
				, Table.NAME
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("operator=%s : row is not found in `%s`.", operator_name, Table.NAME));
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
		 * オペレータ情報リストを取得する
		 * @param connection SQLコネクション
		 * @return オペレータ情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s` FROM `%s`"
				, Column.NAME
				, Column.PASSWORD
				, Column.PUBLISHED
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
		 * オペレータ情報を更新する
		 * @param connection SQLコネクション
		 * @param operator オペレータ情報
		 */
		public final void updateRow(final SqlConnection connection, final Row operator)
		{
			final String sql = String.format(
				"UPDATE `%s` SET `%s`=?,`%s`=NOW() WHERE `%s`=?"
				, Table.NAME
				, Column.PASSWORD
				, Column.UPDATE_TIME
				, Column.NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator.getPassword());
				statement.setString(index++, operator.getName());
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ情報が存在するかどうかを取得する
		 * @param operators オペレータ情報リスト
		 * @param operator_name オペレータ名
		 * @return オペレータ情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operators, final String operator_name)
		{
			for (final Row operator : operators)
			{
				if (operator.getName().equals(operator_name))
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
		private final String name;
		/** パスワード名 */
		private volatile String password;
		/** 公開/非公開 */
		private final boolean published;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.name = result.getString(Column.NAME);
			this.password = result.getString(Column.PASSWORD);
			this.published = result.getBoolean(Column.PUBLISHED);
		}

		/**
		 * オペレータ名を取得する
		 * @return オペレータ名
		 */
		public final String getName()
		{
			return this.name;
		}

		/**
		 * パスワードを設定する
		 * @param password パスワード
		 */
		public final void setPassword(final String password)
		{
			if (password == null)
			{
				throw new NullPointerException();
			}
			this.password = password;
		}

		/**
		 * パスワードを取得する
		 * @return パスワード
		 */
		public final String getPassword()
		{
			return this.password;
		}

		/**
		 * 公開されているかどうかを取得する
		 * @return 公開されている場合はtrueを返す
		 */
		public final boolean isPublished()
		{
			return this.published;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** オペレータ名 */
		public static final String NAME = "name";
		/** パスワード */
		public static final String PASSWORD = "password";
		/** 公開可 */
		public static final String PUBLISHED = "published";
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
