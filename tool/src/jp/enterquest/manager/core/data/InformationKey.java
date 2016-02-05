/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.data;

import jp.enterquest.system.Array;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlResult;
import jp.enterquest.system.SqlStatement;

/**
 * システムキー情報を提供するクラス
 * @author Akinori Nishimura
 */
public class InformationKey
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
	private InformationKey()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** スキーマ名 */
		public static final String SCHEMA = "information_schema";
		/** テーブル名 */
		public static final String NAME = "STATISTICS";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * キー情報リストを取得する
		 * @param connection SQLコネクション
		 * @param table_name テーブル名
		 * @return キー情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection, final String table_name)
		{
			final String sql = String.format(
				"SELECT `%s`,`%s`,`%s`,`%s`,`%s` FROM `%s`.`%s` WHERE `%s`=(SELECT DATABASE()) AND `%s`=?"
				, Column.TABLE_NAME
				, Column.NON_UNIQUE
				, Column.INDEX_NAME
				, Column.SEQ_IN_INDEX
				, Column.COLUMN_NAME
				, Table.SCHEMA
				, Table.NAME
				, Column.TABLE_SCHEMA
				, Column.TABLE_NAME
			);
			final SqlStatement statement = connection.newStatement(sql);
			try
			{
				int index = 0;
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
		 * キー情報が存在するかどうかを取得する
		 * @param keys キー情報リスト
		 * @param column_name カラム名
		 * @return キー情報が存在する場合はtrueを返す
		 */
		public final boolean hasRow(final Array<Row> keys, final String column_name)
		{
			for (final Row key : keys)
			{
				if (key.getColumnName().equals(column_name))
				{
					return true;
				}
			}
			return false;
		}

		/**
		 * キー情報を取得する
		 * @param keys キー情報リスト
		 * @param column_name カラム名
		 * @return キー情報
		 */
		public final Row findRow(final Array<Row> keys, final String column_name)
		{
			for (final Row key : keys)
			{
				if (key.getColumnName().equals(column_name))
				{
					return key;
				}
			}
			throw new RuntimeException(String.format("column=%s : row is not found in `%s`.`%s`.", column_name, Table.SCHEMA, Table.NAME));
		}

		/**
		 * インデックス名リストを取得する
		 * @param keys キー情報リスト
		 * @return インデックス名リスト
		 */
		public final Array<String> getIndexNames(final Array<Row> keys)
		{
			final Array<String> index_names = Array.newInstance();
			for (final Row key : keys)
			{
				final String index_name = key.getIndexName();
				if (!index_names.has(index_name))
				{
					index_names.add(index_name);
				}
			}
			return index_names;
		}

		/**
		 * カラム名を取得する
		 * @param keys キー情報リスト
		 * @param index_name インデックス名
		 * @return カラム名
		 */
		public final String getColumNames(final Array<Row> keys, final String index_name)
		{
			final StringBuilder buffer = new StringBuilder(128);
			boolean first = true;
			for (final Row key : keys)
			{
				if (key.getIndexName().equals(index_name))
				{
					buffer.append(first ? "" : ", ");
					buffer.append(key.getColumnName());
					first = false;
				}
			}
			return buffer.toString();
		}

		/**
		 * プライマリキーかどうかを取得する
		 * @param keys キー情報リスト
		 * @param index_name インデックス名
		 * @return プライマリキーの場合はtrueを返す
		 */
		public final boolean isPrimary(final Array<Row> keys, final String index_name)
		{
			for (final Row key : keys)
			{
				if (key.getIndexName().equals(index_name))
				{
					if (key.isPrimary())
					{
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * ユニークキーかどうかを取得する
		 * @param keys キー情報リスト
		 * @param index_name インデックス名
		 * @return ユニークキーの場合はtrueを返す
		 */
		public final boolean isUnique(final Array<Row> keys, final String index_name)
		{
			for (final Row key : keys)
			{
				if (key.getIndexName().equals(index_name))
				{
					if (key.isUnique())
					{
						return true;
					}
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
		/** プライマリキー */
		private static final String PRIMARY = "PRIMARY";

		/** テーブル名 */
		private final String table_name;
		/** ユニーク/非ユニーク */
		private final boolean unique;
		/** インデックス名 */
		private final String index_name;
		/** カラム名 */
		private final String column_name;

		/**
		 * コンストラクタ
		 * @param result 結果セット
		 */
		private Row(final SqlResult result)
		{
			this.table_name = result.getString(Column.TABLE_NAME);
			this.unique = !result.getBoolean(Column.NON_UNIQUE);
			this.index_name = result.getString(Column.INDEX_NAME);
			this.column_name = result.getString(Column.COLUMN_NAME);
		}

		/**
		 * テーブル名を取得する
		 * @return テーブル名
		 */
		public final String getTableName()
		{
			return this.table_name;
		}

		/**
		 * ユニークかどうかを取得する
		 * @return ユニークな場合はtrueを返す
		 */
		public final boolean isUnique()
		{
			return this.unique;
		}

		/**
		 * プライマリキーかどうかを取得する
		 * @return プライマリキーの場合はtrueを返す
		 */
		public final boolean isPrimary()
		{
			return this.index_name.equals(Row.PRIMARY);
		}

		/**
		 * インデックス名を取得する
		 * @return インデックス名
		 */
		public final String getIndexName()
		{
			return this.index_name;
		}

		/**
		 * カラム名を取得する
		 * @return カラム名
		 */
		public final String getColumnName()
		{
			return this.column_name;
		}
	}

	/**
	 * カラム情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Column
	{
		/** スキーマ名 */
		public static final String TABLE_SCHEMA = "TABLE_SCHEMA";
		/** テーブル名 */
		public static final String TABLE_NAME = "TABLE_NAME";
		/** 非ユニーク/ユニーク */
		public static final String NON_UNIQUE = "NON_UNIQUE";
		/** インデックス名 */
		public static final String INDEX_NAME = "INDEX_NAME";
		/** インデックス値 */
		public static final String SEQ_IN_INDEX = "SEQ_IN_INDEX";
		/** カラム名 */
		public static final String COLUMN_NAME = "COLUMN_NAME";

		/**
		 * @deprecated
		 */
		@Deprecated
		private Column()
		{
		}
	}
}
