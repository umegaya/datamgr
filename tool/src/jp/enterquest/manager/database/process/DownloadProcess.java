/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.process;

import jp.enterquest.manager.core.data.Common;
import jp.enterquest.manager.core.data.InformationColumn;
import jp.enterquest.manager.core.data.ManagedDatabase;
import jp.enterquest.manager.core.data.ManagedTable;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorDatabase;
import jp.enterquest.manager.core.data.OperatorTable;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.process.FileOutputProcess;
import jp.enterquest.manager.core.process.LoginViewProcess;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.system.Array;
import jp.enterquest.system.Data;
import jp.enterquest.system.Hash;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.SqlOrder;

/**
 * ダウンロード処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class DownloadProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public DownloadProcess(final HttpServer server)
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
		final Process process = this.select(request);
		process.run(request, response);
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
			final String request_file_format = request.getParameter("format").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s database=%s table=%s : download file.", request_client, request_operator_name, request_database_name, request_table_name);
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
			if (!selected_operator_database.isDownloadable())
			{
				logger.warning("client=%s operator=%s database=%s : database is not downloadable.", request_client, request_operator_name, request_database_name);
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
			if (!selected_operator_table.isDownloadable())
			{
				logger.warning("client=%s operator=%s database=%s table=%s : operator can not download file.", request_client, request_operator_name, request_database_name, request_table_name);
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

			final ManagedDatabase.Row database = ManagedDatabase.getTable().findRow(databases, request_database_name);
			final SqlConnection select_connection = this.getConnection(database.getResource());
			try
			{
				final ManagedTable.Row table = ManagedTable.getTable().findRow(tables, request_table_name);
				final String filename = String.format("%s.%s.%s", table.getDatabase(), table.getName(), request_file_format);
				final String renumbering_column_name = table.getRenumberingColumn();
				final Array<InformationColumn.Row> columns = InformationColumn.getTable().selectRows(select_connection, request_table_name);
				final Hash<String,Data> conditions = Hash.newInstance();
				final Hash<String,SqlOrder> orders = Hash.newInstance();
				orders.set(renumbering_column_name, SqlOrder.ASC);
				final Data rows = Common.getInstance().selectRows(select_connection, request_table_name, columns, conditions, orders, null, null);
				return new FileOutputProcess(this.getServer(), filename, request_file_format, rows);
			}
			finally
			{
				select_connection.close();
			}
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
