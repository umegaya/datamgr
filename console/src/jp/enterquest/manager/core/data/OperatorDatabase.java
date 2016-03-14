/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

import jp.enterquest.manager.core.name.SpecialUserName;

/**
 * 管理ツールオペレータ別データベース情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class OperatorDatabase
{
	/**
	 * テーブル情報を取得する
	 * @return テーブル情報
	 */
	public static final Table getTable()
	{
		return Table.singleton;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	private OperatorDatabase()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "operator_database";

		/** シングルトンインスタンス */
		private static final Table singleton = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * オペレータ別データベース情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @return オペレータ別データベース情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String operator_name, final String database_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別データベース情報を取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @return オペレータ別データベース情報
		 */
		public final Row selectRow(final SqlConnection connection, final String operator_name, final String database_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, database_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("operator=%s database=%s : row is not found in `%s`.", operator_name, database_name, Table.NAME));
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
		 * オペレータ別データベース情報リストを取得する
		 * @param connection SQLコネクション
		 * @return オペレータ別データベース情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
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
		 * オペレータ別データベース情報リストを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @return オペレータ別データベース情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String operator_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=?"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Table.NAME
				, Column.OPERATOR
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
		 * オペレータ別データベース情報を追加する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 */
		public final void insertRow(final SqlConnection connection, final String operator_name, final String database_name)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Column.NOTES
				, Column.INSERT_TIME
				, Column.UPDATE_TIME
			);
			final SqlStatement statement = connection.newStatement(sql);
			final boolean is_root = operator_name.equals(SpecialUserName.ROOT);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, database_name);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, is_root);
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
		 * オペレータ別データベース情報を更新する
		 * @param connection SQLコネクション
		 * @param operator_database オペレータ別データベース情報
		 */
		public final void updateRow(final SqlConnection connection, final Row operator_database)
		{
			final String sql = String.format(
				"UPDATE `%s` SET `%s`=?,`%s`=NOW() WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.SELECTED
				, Column.UPDATE_TIME
				, Column.OPERATOR
				, Column.DATABASE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setBoolean(index++, operator_database.isSelected());
				statement.setString(index++, operator_database.getOperator());
				statement.setString(index++, operator_database.getDatabase());
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別データベース情報を削除する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 */
		public final void deleteRow(final SqlConnection connection, final String operator_name, final String database_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, database_name);
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別データベース情報が存在するかどうかを取得する
		 * @param operator_databases オペレータ別データベース情報リスト
		 * @param database_name データベース名
		 * @return オペレータ別データベース情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_databases, final String database_name)
		{
			for (final Row operator_database : operator_databases)
			{
				if (operator_database.getDatabase().equals(database_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別データベース情報が存在するかどうかを取得する
		 * @param operator_databases オペレータ別データベース情報リスト
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @return オペレータ別データベース情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_databases, final String operator_name, final String database_name)
		{
			for (final Row operator_database : operator_databases)
			{
				if (operator_database.getOperator().equals(operator_name) && operator_database.getDatabase().equals(database_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別データベース情報を取得する
		 * @param operator_databases オペレータ別データベース情報リスト
		 * @param database_name データベース名
		 * @return オペレータ別データベース情報
		 */
		public final Row findRow(final Array<Row> operator_databases, final String database_name)
		{
			for (final Row operator_database : operator_databases)
			{
				if (operator_database.getDatabase().equals(database_name))
				{
					return operator_database;
				}
			}
			throw new RuntimeException(String.format("database=%s : row is not found in `%s`.", database_name, Table.NAME));
		}

		/**
		 * 選択中のデータベース名を取得する
		 * @param operator_databases オペレータ別データベース情報リスト
		 * @return 選択中のデータベース名
		 */
		public final String findSelectedName(final Array<Row> operator_databases)
		{
			for (final Row operator_database : operator_databases)
			{
				if (operator_database.isSelected() && operator_database.isReadable())
				{
					return operator_database.getDatabase();
				}
			}
			return "";
		}

		/**
		 * 閲覧可能なデータベースが存在するかどうかを取得する
		 * @param operator_databases オペレータ別データベース情報リスト
		 * @return 閲覧可能なデータベースが存在する場合はtrueを返す
		 */
		public final boolean isAnyReadable(final Array<Row> operator_databases)
		{
			for (final Row operator_database : operator_databases)
			{
				if (operator_database.isReadable())
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * 選択中のデータベースが存在するかどうかを取得する
		 * @param operator_databases オペレータ別データベース情報リスト
		 * @return 選択中のデータベースが存在する場合はtrueを返す
		 */
		public final boolean isAnySelected(final Array<Row> operator_databases)
		{
			for (final Row operator_database : operator_databases)
			{
				if (operator_database.isSelected() && operator_database.isReadable())
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
		/** データベース名 */
		private final String database;
		/** 閲覧可/不可 */
		private final boolean readable;
		/** 更新可/不可 */
		private final boolean writable;
		/** ダウンロード可/不可 */
		private final boolean downloadable;
		/** アップロード可/不可 */
		private final boolean uploadable;
		/** 選択中/非選択中 */
		private volatile boolean selected;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.operator = result.getString(Column.OPERATOR);
			this.database = result.getString(Column.DATABASE);
			this.readable = result.getBoolean(Column.READABLE);
			this.writable = result.getBoolean(Column.WRITABLE);
			this.downloadable = result.getBoolean(Column.DOWNLOADABLE);
			this.uploadable = result.getBoolean(Column.UPLOADABLE);
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
		 * データベース名を取得する
		 * @return データベース名
		 */
		public final String getDatabase()
		{
			return this.database;
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
		 * 更新可能かどうかを取得する
		 * @return 更新可能な場合はtrueを返す
		 */
		public final boolean isWritable()
		{
			return this.writable;
		}

		/**
		 * ダウンロード可能かどうかを取得する
		 * @return ダウンロード可能な場合はtrueを返す
		 */
		public final boolean isDownloadable()
		{
			return this.downloadable;
		}

		/**
		 * アップロード可能かどうかを取得する
		 * @return アップロード可能な場合はtrueを返す
		 */
		public final boolean isUploadable()
		{
			return this.uploadable;
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
		/** データベース名 */
		public static final String DATABASE = "database";
		/** 閲覧可否 */
		public static final String READABLE = "readable";
		/** 更新可否 */
		public static final String WRITABLE = "writable";
		/** ダウンロード可否 */
		public static final String DOWNLOADABLE = "downloadable";
		/** アップロード可否 */
		public static final String UPLOADABLE = "uploadable";
		/** 選択可否 */
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
