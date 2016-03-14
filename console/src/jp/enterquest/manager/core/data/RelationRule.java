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
import jp.enterquest.manager.core.data.ManagedTable;


/**
 * 一般的なリレーションのパターンを定義するレコード
 * @author Takehiro Iyatomi
 */
public final class RelationRule
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
	private RelationRule()
	{
	}

	/**
	 * テーブル情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Table
	{
		/** テーブル名 */
		public static final String NAME = "relation_rule";

		/** シングルトンインスタンス */
		private static final Table instance = new Table();

		/**
		 * コンストラクタ
		 */
		private Table()
		{
		}

		/**
		 * リレーション情報リストを取得する
		 * @param connection SQLコネクション
		 * @return リレーション情報リスト
		 */
		public final Array<Row> selectRows(final SqlConnection connection)
		{
			final String sql = String.format(
				"SELECT `%s`, `%s` FROM `%s`"
				, Column.PATTERN
				, Column.COLUMN_NAME
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
	}

	/**
	 * ロウ情報を提供するクラス
	 * @author Akinori Nishimura
	 */
	public static final class Row
	{
		/** パターン */
		private final String pattern;
		/** リンクidとなるカラム名 */
		private final String column_name;

		/**
		 * コンストラクタ
		 * @param result SQLリザルト
		 */
		private Row(final SqlResult result)
		{
			this.pattern = result.getString(Column.PATTERN);
			this.column_name = result.getString(Column.COLUMN_NAME);
		}

		/**
		 * テーブル名を展開し、パターン文字列を生成する
		 */
		public final String formatPattern(final String table)
		{
			return String.format(this.pattern, table);
		}

		public final String getPattern()
		{
			return this.pattern;
		}

		/**
		 * リンクidとなるカラム名を取得する
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
		/** パターン */
		public static final String PATTERN = "pattern";
		/** ラベルとなるカラム名 */
		public static final String COLUMN_NAME = "column_name";

		/**
		 * @deprecated
		 */
		@Deprecated
		private Column()
		{
		}
	}
}
