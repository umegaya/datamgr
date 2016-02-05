/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.Data;
import jp.enterquest.system.DataFactory;
import jp.enterquest.system.Hash;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlOrder;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * 管理ツールオペレータ別カラム情報を提供するクラス
 * @author Akinori Nishimura
 */
public final class OperatorColumn
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
	private OperatorColumn()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "operator_column";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * オペレータ別カラム情報が存在するかどうかを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return オペレータ別カラム情報が存在する場合はtrueを返す
		 */
		public final boolean existsRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"SELECT 1 FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.COLUMN
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別カラム情報リストを取得する
		 * @param connection SQLコネクション
		 * @return オペレータ別カラム情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.COLUMN
				, Column.FILTER_ENABLED
				, Column.FILTER_CONDITION
				, Column.SORT_ENABLED
				, Column.SORT_PRIORITY
				, Column.SORT_ORDER
				, Column.WIDTH
				, Column.HEIGHT
				, Column.VISIBLE
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
		 * オペレータ別カラム情報リストを取得する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @return オペレータ別カラム情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String operator_name, final String database_name, final String table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s`,`%s` FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=? ORDER BY `%s` ASC"
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.COLUMN
				, Column.FILTER_ENABLED
				, Column.FILTER_CONDITION
				, Column.SORT_ENABLED
				, Column.SORT_PRIORITY
				, Column.SORT_ORDER
				, Column.WIDTH
				, Column.HEIGHT
				, Column.VISIBLE
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.SORT_ORDER
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
		 * オペレータ別カラム情報を追加する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 */
		public final void insertRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"INSERT INTO `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW(),`%s`=NOW()"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.COLUMN
				, Column.FILTER_ENABLED
				, Column.FILTER_CONDITION
				, Column.SORT_ENABLED
				, Column.SORT_PRIORITY
				, Column.SORT_ORDER
				, Column.WIDTH
				, Column.HEIGHT
				, Column.VISIBLE
				, Column.NOTES
				, Column.INSERT_TIME
				, Column.UPDATE_TIME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
				statement.setString(index++, database_name);
				statement.setString(index++, table_name);
				statement.setString(index++, column_name);
				statement.setBoolean(index++, false);
				statement.setString(index++, "");
				statement.setBoolean(index++, false);
				statement.setInt32(index++, 0);
				statement.setString(index++, SqlOrder.ASC.getName());
				statement.setInt32(index++, 100);
				statement.setInt32(index++, 12);
				statement.setBoolean(index++, true);
				statement.setString(index++, "");
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別カラム情報を更新する
		 * @param connection SQLコネクション
		 * @param operator_column オペレータ別カラム情報
		 */
		public final void updateRow(final SqlConnection connection, final Row operator_column)
		{
			final String sql = String.format(
				"UPDATE `%s` SET `%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=?,`%s`=NOW() WHERE `%s`=? AND `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.FILTER_ENABLED
				, Column.FILTER_CONDITION
				, Column.SORT_ENABLED
				, Column.SORT_PRIORITY
				, Column.SORT_ORDER
				, Column.WIDTH
				, Column.HEIGHT
				, Column.VISIBLE
				, Column.UPDATE_TIME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.COLUMN
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setBoolean(index++, operator_column.isFilterEnabled());
				statement.setString(index++, operator_column.getFilterCondition());
				statement.setBoolean(index++, operator_column.isSortEnabled());
				statement.setInt32(index++, operator_column.getSortPriority());
				statement.setString(index++, operator_column.getSortOrder());
				statement.setInt32(index++, operator_column.getWidth());
				statement.setInt32(index++, operator_column.getHeight());
				statement.setBoolean(index++, operator_column.isVisible());
				statement.setString(index++, operator_column.getOperator());
				statement.setString(index++, operator_column.getDatabase());
				statement.setString(index++, operator_column.getTable());
				statement.setString(index++, operator_column.getColumn());
				statement.executeUpdate();
			}
			finally
			{
				statement.close();
			}
		}

		/**
		 * オペレータ別カラム情報を削除する
		 * @param connection SQLコネクション
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 */
		public final void deleteRow(final SqlConnection connection, final String operator_name, final String database_name, final String table_name, final String column_name)
		{
			final String sql = String.format(
				"DELETE FROM `%s` WHERE `%s`=? AND `%s`=? AND `%s`=? AND `%s`=?"
				, Table.NAME
				, Column.OPERATOR
				, Column.DATABASE
				, Column.TABLE
				, Column.COLUMN
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
				statement.setString(index++, operator_name);
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
		 * オペレータ別カラム情報が存在するかどうかを取得する
		 * @param operator_columns オペレータ別カラム情報リスト
		 * @param column_name カラム名
		 * @return オペレータ別カラム情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_columns, final String column_name)
		{
			for (final Row operator_column : operator_columns)
			{
				if (operator_column.getColumn().equals(column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別カラム情報が存在するかどうかを取得する
		 * @param operator_columns オペレータ別カラム情報リスト
		 * @param operator_name オペレータ名
		 * @param database_name データベース名
		 * @param table_name テーブル名
		 * @param column_name カラム名
		 * @return オペレータ別カラム情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> operator_columns, final String operator_name, final String database_name, final String table_name, final String column_name)
		{
			for (final Row operator_column : operator_columns)
			{
				if (operator_column.getOperator().equals(operator_name)
					&& operator_column.getDatabase().equals(database_name)
					&& operator_column.getTable().equals(table_name)
					&& operator_column.getColumn().equals(column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * オペレータ別カラム情報を取得する
		 * @param operator_columns オペレータ別カラム情報リスト
		 * @param column_name カラム名
		 * @return オペレータ別カラム情報
		 */
		public final Row findRow(final Array<Row> operator_columns, final String column_name)
		{
			for (final Row operator_column : operator_columns)
			{
				if (operator_column.getColumn().equals(column_name))
				{
					return operator_column;
				}
			}
			throw new RuntimeException(String.format("column=%s : row is not found in `%s`", column_name, Table.NAME));
		}

		/**
		 * 条件ハッシュを取得する
		 * @param operator_columns オペレータ別カラム情報リスト
		 * @return 条件ハッシュ
		 */
		public final Hash<String,Data> getConditions(final Array<Row> operator_columns)
		{
			final Hash<String,Data> conditions = Hash.newInstance();
			for (final Row operator_column : operator_columns)
			{
				if (operator_column.isFilterEnabled())
				{
					final String column_name = operator_column.getColumn();
					final String filter_condition = operator_column.getFilterCondition();
					conditions.set(column_name, DataFactory.getInstance().newString(filter_condition));
				}
			}
			return conditions;
		}

		/**
		 * 順序ハッシュを取得する
		 * @param operator_columns オペレータ別カラム情報リスト
		 * @return 順序ハッシュ
		 */
		public final Hash<String,SqlOrder> getOrders(final Array<Row> operator_columns)
		{
			final SqlOrder asc = SqlOrder.ASC;
			final SqlOrder desc = SqlOrder.DESC;
			final Hash<String,SqlOrder> orders = Hash.newInstance();
			for (final Row operator_column : operator_columns)
			{
				if (operator_column.isSortEnabled())
				{
					final String column_name = operator_column.getColumn();
					final String sort_order = operator_column.getSortOrder();
					orders.set(column_name, (!desc.getName().equalsIgnoreCase(sort_order) ? asc : desc));
				}
			}
			return orders;
		}
	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row extends Object
	{
		/** 最小の表示幅 */
		private static final int MIN_WIDTH = 10;
		/** 最小の表示高さ */
		private static final int MIN_HEIGHT = 10;

		/** オペレータ名 */
		private final String operator;
		/** データベース名 */
		private final String database;
		/** テーブル名 */
		private final String table;
		/** カラム名 */
		private final String column;
		/** フィルタ有効 */
		private volatile boolean filter_enabled;
		/** フィルタ条件 */
		private volatile String filter_condition;
		/** ソート有効 */
		private volatile boolean sort_enabled;
		/** ソート優先度 */
		private volatile int sort_priority;
		/** ソート順序 */
		private volatile String sort_order;
		/** 表示幅 */
		private volatile int width;
		/** 表示高さ */
		private volatile int height;
		/** 表示可 */
		private volatile boolean visible;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.operator = result.getString(Column.OPERATOR);
			this.database = result.getString(Column.DATABASE);
			this.table = result.getString(Column.TABLE);
			this.column = result.getString(Column.COLUMN);
			this.filter_enabled = result.getBoolean(Column.FILTER_ENABLED);
			this.filter_condition = result.getString(Column.FILTER_CONDITION);
			this.sort_enabled = result.getBoolean(Column.SORT_ENABLED);
			this.sort_priority = result.getInt32(Column.SORT_PRIORITY);
			this.sort_order = result.getString(Column.SORT_ORDER);
			this.width = result.getInt32(Column.WIDTH);
			this.height = result.getInt32(Column.HEIGHT);
			this.visible = result.getBoolean(Column.VISIBLE);
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
		 * カラム名を取得する
		 * @return カラム名
		 */
		public final String getColumn()
		{
			return this.column;
		}

		/**
		 * フィルタが有効かどうかを設定する
		 * @param filter_enabled フィルタが有効な場合はtrueを指定する
		 */
		public final void isFilterEnabled(final boolean filter_enabled)
		{
			this.filter_enabled = filter_enabled;
		}

		/**
		 * フィルタが有効かどうかを取得する
		 * @return フィルタが有効な場合はtrueを返す
		 */
		public final boolean isFilterEnabled()
		{
			return this.filter_enabled;
		}

		/**
		 * フィルタ条件を設定する
		 * @param filter_condition フィルタ条件
		 */
		public final void setFilterCondition(final String filter_condition)
		{
			this.filter_condition = filter_condition;
		}

		/**
		 * フィルタ条件を取得する
		 * @return フィルタ条件
		 */
		public final String getFilterCondition()
		{
			return this.filter_condition;
		}

		/**
		 * ソートが有効かどうかを設定する
		 * @param sort_enabled ソートが有効な場合はtrueを指定する
		 */
		public final void isSortEnabled(final boolean sort_enabled)
		{
			this.sort_enabled = sort_enabled;
		}

		/**
		 * ソートが有効かどうかを取得する
		 * @return ソートが有効な場合はtrueを返す
		 */
		public final boolean isSortEnabled()
		{
			return this.sort_enabled;
		}

		/**
		 * ソート優先度を設定する
		 * @param sort_priority ソート優先度
		 */
		public final void setSortPriority(final int sort_priority)
		{
			this.sort_priority = sort_priority;
		}

		/**
		 * ソート優先度を取得する
		 * @return ソート優先度
		 */
		public final int getSortPriority()
		{
			return this.sort_priority;
		}

		/**
		 * ソート順序を設定する
		 * @param sort_order ソート順序
		 */
		public final void setSortOrder(final String sort_order)
		{
			final SqlOrder asc = SqlOrder.ASC;
			final SqlOrder desc = SqlOrder.DESC;
			this.sort_order = (!desc.getName().equalsIgnoreCase(sort_order) ? asc.getName() : desc.getName());
		}

		/**
		 * ソート順序を取得する
		 * @return ソート順序
		 */
		public final String getSortOrder()
		{
			return this.sort_order;
		}

		/**
		 * 表示幅を設定する
		 * @param width 表示幅
		 */
		public final void setWidth(final int width)
		{
			this.width = Math.max(width, Row.MIN_WIDTH);
		}

		/**
		 * 表示幅を取得する
		 * @return 表示幅
		 */
		public final int getWidth()
		{
			return this.width;
		}

		/**
		 * 表示高さを設定する
		 * @param height 表示高さ
		 */
		public final void setHeight(final int height)
		{
			this.height = Math.max(height, Row.MIN_HEIGHT);
		}

		/**
		 * 表示高さを取得する
		 * @return 表示高さ
		 */
		public final int getHeight()
		{
			return this.height;
		}

		/**
		 * 表示可能かどうかを設定する
		 * @param visible 表示可能な場合はtrueを指定する
		 */
		public final void isVisible(final boolean visible)
		{
			this.visible = visible;
		}

		/**
		 * 表示可能かどうかを取得する
		 * @return 表示可能な場合はtrueを返す
		 */
		public final boolean isVisible()
		{
			return this.visible;
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
		/** カラム名 */
		public static final String COLUMN = "column";
		/** フィルタ有効 */
		public static final String FILTER_ENABLED = "filter_enabled";
		/** フィルタ条件 */
		public static final String FILTER_CONDITION = "filter_condition";
		/** ソート有効 */
		public static final String SORT_ENABLED = "sort_enabled";
		/** ソート優先度 */
		public static final String SORT_PRIORITY = "sort_priority";
		/** ソート順序 */
		public static final String SORT_ORDER = "sort_order";
		/** 表示幅 */
		public static final String WIDTH = "width";
		/** 表示高さ */
		public static final String HEIGHT = "height";
		/** 表示可 */
		public static final String VISIBLE = "visible";
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
