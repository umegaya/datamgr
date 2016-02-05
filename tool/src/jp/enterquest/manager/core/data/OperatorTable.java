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
 * 管理ツールオペレータ別テーブル情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class OperatorTable
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
	private OperatorTable()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "operator_table";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * オペレータ別テーブル情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return オペレータ別テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別テーブル情報を取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return オペレータ別テーブル情報
		 */
		public final Row selectRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Column.OFFSET
				, Column.LIMIT
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				final SqlResult result = statement.executeQuery();
				try
				{
					if (result.next())
					{
						return new Row(result);
					}
					throw new RuntimeException(String.format("operator=%s database=%s table=%s : row is not found in `%s`.", operator_name, database_name, table_name));
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
		 * オペレータ別テーブル情報リストを取得する
		 * @param connection SQLコネクション
		 * @return オペレータ別テーブル情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Column.OFFSET
				, Column.LIMIT
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
		 * オペレータ別テーブル情報リストを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @return オペレータ別テーブル情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String operator_name, final String database_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=?"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Column.OFFSET
				, Column.LIMIT
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
		 * オペレータ別テーブル情報を追加する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 */
		public final void insertRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.READABLE
				, Column.WRITABLE
				, Column.DOWNLOADABLE
				, Column.UPLOADABLE
				, Column.SELECTED
				, Column.OFFSET
				, Column.LIMIT
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
				statement.setString(index++, table_name);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, is_root);
				statement.setBoolean(index++, false);
				statement.setInt32(index++, 0);
				statement.setInt32(index++, 100);
				statement.setString(index++, "");
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別テーブル情報を更新する
		 * @param connection SQLコネクション
		 * @param operator_table オペレータ別テーブル情報
		 */
		public final void updateRow(final SqlConnection connection, final Row operator_table)
		{
			final String sql = String.format(
				"UPDATE `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=NOW() WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.SELECTED
				, Column.OFFSET
				, Column.LIMIT
				, Column.UPDATE_TIME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setBoolean(index++, operator_table.isSelected());
				statement.setInt32(index++, operator_table.getOffset());
				statement.setInt32(index++, operator_table.getLimit());
				statement.setString(index++, operator_table.getOperator());
				statement.setString(index++, operator_table.getDatabase());
				statement.setString(index++, operator_table.getTable());
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別テーブル情報を削除する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 */
		public final void deleteRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別テーブル情報が存在するかどうかを取得する
		 * @param operator_tables オペレータ別テーブル情報リスト
		 * @param table_name テーブル名
		 * @return オペレータ別テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_tables, final String table_name)
		{
			for (final Row operator_table : operator_tables)
			{
				if (operator_table.getTable().equals(table_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別テーブル情報が存在するかどうかを取得する
		 * @param operator_tables オペレータ別テーブル情報リスト
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return オペレータ別テーブル情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_tables, final String operator_name, final String database_name, final String table_name)
		{
			for (final Row operator_table : operator_tables)
			{
				if (operator_table.getOperator().equals(operator_name) && operator_table.getDatabase().equals(database_name) && operator_table.getTable().equals(table_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別テーブル情報を取得する
		 * @param operator_tables オペレータ別テーブル情報リスト
		 * @param table_name テーブル名
		 * @return オペレータ別テーブル情報
		 */
		public final Row findRow(final Array<Row> operator_tables, final String table_name)
		{
			for (final Row operator_table : operator_tables)
			{
				if (operator_table.getTable().equals(table_name))
				{
					return operator_table;
				}
			}
			throw new RuntimeException(String.format("table=%s : row is not found in `%s`.", table_name, Table.NAME));
		}

		/**
		 * 選択中のテーブル名を取得する
		 * @param operator_tables オペレータ別テーブル情報リスト
		 * @return 選択中のテーブル名
		 */
		public final String findSelectedName(final Array<Row> operator_tables)
		{
			for (final Row operator_table : operator_tables)
			{
				if (operator_table.isSelected() && operator_table.isReadable())
				{
					return operator_table.getTable();
				}
			}
			return "";
		}

		/**
		 * 閲覧可能なテーブルが存在するかどうかを取得する
		 * @param operator_tables オペレータ別テーブル情報リスト
		 * @return 閲覧可能なテーブルが存在する場合はtrueを返す
		 */
		public final boolean isAnyReadable(final Array<Row> operator_tables)
		{
			for (final Row operator_table : operator_tables)
			{
				if (operator_table.isReadable())
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * 選択中のテーブルが存在するかどうかを取得する
		 * @param operator_tables オペレータ別テーブル情報リスト
		 * @return 選択中のテーブルが存在する場合はtrueを返す
		 */
		public final boolean isAnySelected(final Array<Row> operator_tables)
		{
			for (final Row operator_table : operator_tables)
			{
				if (operator_table.isSelected() && operator_table.isReadable())
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
		/** テーブル名 */
		private final String table;
		/** 閲覧可 */
		private final boolean readable;
		/** 更新可 */
		private final boolean writable;
		/** ダウンロード可 */
		private final boolean downloadable;
		/** アップロード可 */
		private final boolean uploadable;
		/** 選択中 */
		private volatile boolean selected;
		/** 表示オフセット */
		private volatile int offset;
		/** 表示リミット */
		private volatile int limit;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.operator = result.getString(Column.OPERATOR);
			this.database = result.getString(Column.DATABASE);
			this.table = result.getString(Column.TABLE);
			this.readable = result.getBoolean(Column.READABLE);
			this.writable = result.getBoolean(Column.WRITABLE);
			this.downloadable = result.getBoolean(Column.DOWNLOADABLE);
			this.uploadable = result.getBoolean(Column.UPLOADABLE);
			this.selected = result.getBoolean(Column.SELECTED);
			this.offset = result.getInt32(Column.OFFSET);
			this.limit = result.getInt32(Column.LIMIT);
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
		 * テーブル名を取得する
		 * @return テーブル名
		 */
		public final String getTable()
		{
			return this.table;
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

		/**
		 * 表示オフセットを設定する
		 * @param offset 表示オフセット
		 */
		public final void setOffset(final int offset)
		{
			this.offset = Math.max(offset, 0);
		}

		/**
		 * 表示オフセットを取得する
		 * @return 表示オフセット
		 */
		public final int getOffset()
		{
			return this.offset;
		}

		/**
		 * 表示リミットを設定する
		 * @param limit 表示リミット
		 */
		public final void setLimit(final int limit)
		{
			this.limit = Math.max(limit, 0);
		}

		/**
		 * 表示リミットを取得する
		 * @return 表示リミット
		 */
		public final int getLimit()
		{
			return this.limit;
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
		/** テーブル名 */
		public static final String TABLE = "table";
		/** 閲覧可 */
		public static final String READABLE = "readable";
		/** 更新可 */
		public static final String WRITABLE = "writable";
		/** ダウンロード可 */
		public static final String DOWNLOADABLE = "downloadable";
		/** アップロード可 */
		public static final String UPLOADABLE = "uploadable";
		/** 選択中 */
		public static final String SELECTED = "selected";
		/** 表示オフセット */
		public static final String OFFSET = "offset";
		/** 表示リミット */
		public static final String LIMIT = "limit";
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
