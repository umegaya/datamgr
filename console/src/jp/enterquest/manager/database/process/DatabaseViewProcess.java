/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.process;

import jp.enterquest.manager.core.data.Common;
import jp.enterquest.manager.core.data.InformationColumn;
import jp.enterquest.manager.core.data.InformationKey;
import jp.enterquest.manager.core.data.ManagedColumn;
import jp.enterquest.manager.core.data.ManagedDatabase;
import jp.enterquest.manager.core.data.ManagedTable;
import jp.enterquest.manager.core.data.Menu;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorColumn;
import jp.enterquest.manager.core.data.OperatorDatabase;
import jp.enterquest.manager.core.data.OperatorMenu;
import jp.enterquest.manager.core.data.OperatorTable;
import jp.enterquest.manager.core.data.Relation;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.process.ForwardProcess;
import jp.enterquest.manager.core.process.LoginViewProcess;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.manager.database.name.ResourceName;
import jp.enterquest.system.Array;
import jp.enterquest.system.CharacterEncoding;
import jp.enterquest.system.Data;
import jp.enterquest.system.Hash;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.MimeType;
import jp.enterquest.system.ReaderStream;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlOrder;
import jp.enterquest.system.TextReader;

/**
 * データベース管理画面表示処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class DatabaseViewProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public DatabaseViewProcess(final HttpServer server)
	{
		super(server);
	}

	/**
	 * リクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	@Override
	public final void run(final HttpServerRequest request, final HttpServerResponse response)
	{
		final Process next_process = this.view(request);
		next_process.run(request, response);
	}

	/**
	 * データベース管理画面表示処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process view(final HttpServerRequest request)
	{
		final SqlConnection manager_connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = this.getOperatorName(request);

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s : database view.", request_client, request_operator_name);
			}

			if (!Operator.getTable().existsRow(manager_connection, request_operator_name))
			{
				logger.warning("client=%s operator=%s : operator is not found.", request_client, request_operator_name);
				return new LoginViewProcess(this.getServer());
			}
			final Operator.Row operator = Operator.getTable().selectRow(manager_connection, request_operator_name);
			if (!operator.isPublished())
			{
				logger.warning("client=%s operator=%s : operator is not published.", request_client, request_operator_name);
				return new LoginViewProcess(this.getServer());
			}

			final Array<Menu.Row> menus = Menu.getTable().selectRows(manager_connection);
			final Array<OperatorMenu.Row> operator_menus = OperatorMenu.getTable().selectRows(manager_connection, request_operator_name);

			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(manager_connection);
			final Array<OperatorDatabase.Row> operator_databases = OperatorDatabase.getTable().selectRows(manager_connection, request_operator_name);
			if (!OperatorDatabase.getTable().isAnySelected(operator_databases))
			{
				for (final OperatorDatabase.Row operator_database : operator_databases)
				{
					if (operator_database.isSelected())
					{
						operator_database.isSelected(false);
						OperatorDatabase.getTable().updateRow(manager_connection, operator_database);
					}
				}
				for (final OperatorDatabase.Row operator_database : operator_databases)
				{
					if (operator_database.isReadable())
					{
						operator_database.isSelected(true);
						OperatorDatabase.getTable().updateRow(manager_connection, operator_database);
						break;
					}
				}
			}

			final String selected_database_name = OperatorDatabase.getTable().findSelectedName(operator_databases);
			final Array<ManagedTable.Row> tables = ManagedTable.getTable().selectRows(manager_connection);
			final Array<OperatorTable.Row> operator_tables = OperatorTable.getTable().selectRows(manager_connection, request_operator_name, selected_database_name);
			if (!OperatorTable.getTable().isAnySelected(operator_tables))
			{
				for (final OperatorTable.Row operator_table : operator_tables)
				{
					if (operator_table.isSelected())
					{
						operator_table.isSelected(false);
						OperatorTable.getTable().updateRow(manager_connection, operator_table);
					}
				}
				for (final OperatorTable.Row operator_table : operator_tables)
				{
					if (operator_table.isReadable())
					{
						operator_table.isSelected(true);
						OperatorTable.getTable().updateRow(manager_connection, operator_table);
						break;
					}
				}
			}

			final String selected_table_name = OperatorTable.getTable().findSelectedName(operator_tables);
			final boolean database_enabled = this.isDatabaseEnabled(databases, operator_databases, selected_database_name);
			final boolean table_enabled = this.isTableEnabled(tables, operator_tables, selected_table_name);
			final boolean selected = database_enabled && table_enabled;

			request.setAttribute("baseUrl", ((String)request.getAttribute("url")).replaceFirst("/[\\w]+$", ""));
			logger.info("urls:%s %s", request.getAttribute("url"), request.getAttribute("baseUrl"));
			request.setAttribute("operator-name", request_operator_name);
			request.setAttribute("menus", menus);
			request.setAttribute("operator-menus", operator_menus);
			request.setAttribute("databases", databases);
			request.setAttribute("operator-databases", operator_databases);
			request.setAttribute("tables", tables);
			request.setAttribute("operator-tables", operator_tables);
			request.setAttribute("selected", selected);

			if (selected)
			{
				final ManagedDatabase.Row database = ManagedDatabase.getTable().findRow(databases, selected_database_name);
				final OperatorDatabase.Row operator_database = OperatorDatabase.getTable().findRow(operator_databases, selected_database_name);
				final ManagedTable.Row table = ManagedTable.getTable().findRow(tables, selected_table_name);
				final OperatorTable.Row operator_table = OperatorTable.getTable().findRow(operator_tables, selected_table_name);
				final Array<ManagedColumn.Row> columns = ManagedColumn.getTable().selectRows(manager_connection, selected_database_name, selected_table_name);
				final Array<OperatorColumn.Row> operator_columns = OperatorColumn.getTable().selectRows(manager_connection, request_operator_name, selected_database_name, selected_table_name);

				final SqlConnection select_connection = this.getConnection(database.getResource());
				try
				{
					final Array<InformationColumn.Row> information_columns = InformationColumn.getTable().selectRows(select_connection, selected_table_name);
					final Array<InformationKey.Row> information_keys = InformationKey.getTable().selectRows(select_connection, selected_table_name);

					final Hash<String,Data> conditions = OperatorColumn.getTable().getConditions(operator_columns);
					final Hash<String,SqlOrder> orders = OperatorColumn.getTable().getOrders(operator_columns);
					final int limit = operator_table.getLimit();
					final int offset = operator_table.getOffset();
					final int row_total_count = Common.getInstance().selectCount(select_connection, selected_table_name);
					final int row_filter_count = Common.getInstance().selectCount(select_connection, selected_table_name, information_columns, conditions);
					final Data rows = Common.getInstance().selectRows(select_connection, selected_table_name, information_columns, conditions, orders, limit, offset);

					final Array<Relation.Row> relations = Relation.getTable().selectRows(manager_connection, selected_database_name, selected_table_name);
					final Array<ManagedColumn.Row> relation_columns = Array.newInstance();
					for (final Relation.Row relation : relations)
					{
						final String parent_table_name = relation.getParentTable();
						final String parent_column_name = relation.getParentColumn();
						final ManagedColumn.Row parent_column = ManagedColumn.getTable().selectRow(manager_connection, selected_database_name, parent_table_name, parent_column_name);
						relation_columns.add(parent_column);
					}
					final Hash<String,Data> relation_rows = Hash.newInstance();
					for (final Relation.Row relation : relations)
					{
						final String parent_table_name = relation.getParentTable();
						if (!relation_rows.has(parent_table_name))
						{
							final Array<OperatorColumn.Row> parent_operator_columns = OperatorColumn.getTable().selectRows(manager_connection, request_operator_name, selected_database_name, parent_table_name);
							final Array<InformationColumn.Row> parent_information_columns = InformationColumn.getTable().selectRows(select_connection, parent_table_name);
							final Hash<String,Data> parent_conditions = Hash.newInstance();
							final Hash<String,SqlOrder> parent_orders = OperatorColumn.getTable().getOrders(parent_operator_columns);
							final Data parent_rows = Common.getInstance().selectRows(select_connection, parent_table_name, parent_information_columns, parent_conditions, parent_orders, null, null);
							relation_rows.set(parent_table_name, parent_rows);
						}
					}

					request.setAttribute("selected-database", database);
					request.setAttribute("selected-operator-database", operator_database);
					request.setAttribute("selected-table", table);
					request.setAttribute("selected-operator-table", operator_table);
					request.setAttribute("columns", columns);
					request.setAttribute("operator-columns", operator_columns);
					request.setAttribute("information-columns", information_columns);
					request.setAttribute("information-keys", information_keys);
					request.setAttribute("row-total-count", row_total_count);
					request.setAttribute("row-filter-count", row_filter_count);
					request.setAttribute("rows", rows);
					request.setAttribute("relations", relations);
					request.setAttribute("relation-columns", relation_columns);
					request.setAttribute("relation-rows", relation_rows);
				}
				finally
				{
					select_connection.close();
				}
			}

			return new ForwardProcess(this.getServer(), ResourceName.DATABASE_VIEW);
		}
		finally
		{
			manager_connection.close();
		}
	}

	/**
	 * 選択中のデータベースが有効かどうかを取得する
	 * @param databases データベース情報リスト
	 * @param operator_databases オペレータ別データベース情報リスト
	 * @param database_name データベース名
	 * @return 選択中のデータベースが有効な場合はtrueを返す
	 */
	private final boolean isDatabaseEnabled(final Array<ManagedDatabase.Row> databases, final Array<OperatorDatabase.Row> operator_databases, final String database_name)
	{
		if (ManagedDatabase.getTable().hasRow(databases, database_name))
		{
			if (OperatorDatabase.getTable().hasRow(operator_databases, database_name))
			{
				final OperatorDatabase.Row operator_table = OperatorDatabase.getTable().findRow(operator_databases, database_name);
				if (operator_table.isSelected() && operator_table.isReadable())
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 選択中のテーブルが有効かどうかを取得する
	 * @param tables テーブル情報リスト
	 * @param operator_tables オペレータ別テーブル情報リスト
	 * @param table_name テーブル名
	 * @return
	 */
	private final boolean isTableEnabled(final Array<ManagedTable.Row> tables, final Array<OperatorTable.Row> operator_tables, final String table_name)
	{
		if (ManagedTable.getTable().hasRow(tables, table_name))
		{
			if (OperatorTable.getTable().hasRow(operator_tables, table_name))
			{
				final OperatorTable.Row operator_table = OperatorTable.getTable().findRow(operator_tables, table_name);
				if (operator_table.isSelected() && operator_table.isReadable())
				{
					return true;
				}
			}
		}
		return false;
	}
}
