/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.process;

import jp.enterquest.manager.core.data.ManagedDatabase;
import jp.enterquest.manager.core.data.ManagedTable;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorColumn;
import jp.enterquest.manager.core.data.OperatorDatabase;
import jp.enterquest.manager.core.data.OperatorTable;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.process.LoginViewProcess;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.system.Array;
import jp.enterquest.system.HtmlDecoder;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;

/**
 * レコード検索処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class SelectProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public SelectProcess(final HttpServer server)
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
		final Process next_process = this.select(request);
		next_process.run(request, response);
	}

	/**
	 * レコード検索処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process select(final HttpServerRequest request)
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = request.getParameter("operator").asString();
			final String request_database_name = request.getParameter("database").asString();
			final String request_table_name = request.getParameter("table").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s database=%s table=%s : select rows.", request_client, request_operator_name, request_database_name, request_table_name);
			}

			if (!Operator.getTable().existsRow(connection, request_operator_name))
			{
				logger.warning("client=%s operator=%s : operator is not found.", request_client, request_operator_name);
				return new LoginViewProcess(this.getServer());
			}
			final Operator.Row operator = Operator.getTable().selectRow(connection, request_operator_name);
			if (!operator.isPublished())
			{
				logger.warning("client=%s operator=%s : operator is not published.", request_client, request_operator_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(connection);
			if (!ManagedDatabase.getTable().hasRow(databases, request_database_name))
			{
				logger.warning("client=%s operator=%s database=%s : database is not found.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<OperatorDatabase.Row> operator_databases = OperatorDatabase.getTable().selectRows(connection, request_operator_name);
			if (!OperatorDatabase.getTable().hasRow(operator_databases, request_database_name))
			{
				logger.warning("client=%s operator=%s database=%s : operator database is not found.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final OperatorDatabase.Row selected_operator_database = OperatorDatabase.getTable().findRow(operator_databases, request_database_name);
			if (!selected_operator_database.isReadable())
			{
				logger.warning("client=%s operator=%s database=%s : database is not readable.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<ManagedTable.Row> tables = ManagedTable.getTable().selectRows(connection, request_database_name);
			if (!ManagedTable.getTable().hasRow(tables, request_table_name))
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not found.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<OperatorTable.Row> operator_tables = OperatorTable.getTable().selectRows(connection, request_operator_name, request_database_name);
			if (!OperatorTable.getTable().hasRow(operator_tables, request_table_name))
			{
				logger.warning("client=%s operator=%s database=%s table=%s : operator table is not found.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			final OperatorTable.Row selected_operator_table = OperatorTable.getTable().findRow(operator_tables, request_table_name);
			if (!selected_operator_table.isReadable())
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not readable.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}

			for (final OperatorDatabase.Row operator_database : operator_databases)
			{
				if (operator_database.isSelected() && operator_database != selected_operator_database)
				{
					operator_database.isSelected(false);
					OperatorDatabase.getTable().updateRow(connection, operator_database);
				}
			}
			if (!selected_operator_database.isSelected())
			{
				selected_operator_database.isSelected(true);
				OperatorDatabase.getTable().updateRow(connection, selected_operator_database);
			}

			for (final OperatorTable.Row operator_table : operator_tables)
			{
				if (operator_table.isSelected() && operator_table != selected_operator_table)
				{
					operator_table.isSelected(false);
					OperatorTable.getTable().updateRow(connection, operator_table);
				}
			}
			if (!selected_operator_table.isSelected())
			{
				selected_operator_table.isSelected(true);
				OperatorTable.getTable().updateRow(connection, selected_operator_table);
			}
			if (request.hasParameter("offset"))
			{
				final int offset = request.getParameter("offset").asInt32();
				if (selected_operator_table.getOffset() != offset)
				{
					selected_operator_table.setOffset(offset);
					OperatorTable.getTable().updateRow(connection, selected_operator_table);
				}
			}
			if (request.hasParameter("limit"))
			{
				final int limit = request.getParameter("limit").asInt32();
				if (selected_operator_table.getLimit() != limit)
				{
					selected_operator_table.setOffset(0);
					selected_operator_table.setLimit(limit);
					OperatorTable.getTable().updateRow(connection, selected_operator_table);
				}
			}

			final Array<OperatorColumn.Row> operator_columns = OperatorColumn.getTable().selectRows(connection, request_operator_name, request_database_name, request_table_name);
			for (final OperatorColumn.Row operator_column : operator_columns)
			{
				final String column_name = operator_column.getColumn();

				final String filter_enabled_key = String.format("filter-enabled:%s", column_name);
				final String filter_condition_key = String.format("filter-condition:%s", column_name);
				final String sort_enabled_key = String.format("sort-enabled:%s", column_name);
				final String sort_priority_key = String.format("sort-priority:%s", column_name);
				final String sort_order_key = String.format("sort-order:%s", column_name);
				final String width_key = String.format("width:%s", column_name);
				final String height_key = String.format("height:%s", column_name);
				final String visible_key = String.format("visible:%s", column_name);

				final boolean filter_enabled = request.hasParameter(filter_enabled_key);
				final String filter_condition = (request.hasParameter(filter_condition_key) ? HtmlDecoder.getInstance().decode(request.getParameter(filter_condition_key).asString()) : "");
				final boolean sort_enabled = request.hasParameter(sort_enabled_key);
				final int sort_priority = request.getParameter(sort_priority_key).asInt32();
				final String sort_order = request.getParameter(sort_order_key).asString();
				final int width = request.getParameter(width_key).asInt32();
				final int height = request.getParameter(height_key).asInt32();
				final boolean visible = request.hasParameter(visible_key);

				operator_column.isFilterEnabled(filter_enabled);
				operator_column.setFilterCondition(filter_condition);
				operator_column.isSortEnabled(sort_enabled);
				operator_column.setSortPriority(sort_priority);
				operator_column.setSortOrder(sort_order);
				operator_column.setWidth(width);
				operator_column.setHeight(height);
				operator_column.isVisible(visible);

				OperatorColumn.getTable().updateRow(connection, operator_column);
			}

			return new DatabaseViewProcess(this.getServer());
		}
		catch (final RuntimeException cause)
		{
			connection.rollback();
			throw cause;
		}
		finally
		{
			connection.commit();
			connection.close();
		}
	}
}
