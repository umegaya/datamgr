/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.process;

import jp.enterquest.manager.core.data.Common;
import jp.enterquest.manager.core.data.InformationColumn;
import jp.enterquest.manager.core.data.InformationKey;
import jp.enterquest.manager.core.data.ManagedDatabase;
import jp.enterquest.manager.core.data.ManagedTable;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorDatabase;
import jp.enterquest.manager.core.data.OperatorTable;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.process.LoginViewProcess;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.system.Array;
import jp.enterquest.system.Data;
import jp.enterquest.system.DataFactory;
import jp.enterquest.system.Hash;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;

/**
 * レコード削除処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class DeleteProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public DeleteProcess(final HttpServer server)
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
		final Process next_process = this.delete(request);
		next_process.run(request, response);
	}

	/**
	 * レコード削除処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process delete(final HttpServerRequest request)
	{
		final SqlConnection manager_connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = request.getParameter("operator").asString();
			final String request_database_name = request.getParameter("database").asString();
			final String request_table_name = request.getParameter("table").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s database=%s table=%s : delete row.", request_client, request_operator_name, request_database_name, request_table_name);
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
			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(manager_connection);
			if (!ManagedDatabase.getTable().hasRow(databases, request_database_name))
			{
				logger.warning("client=%s operator=%s database=%s : database is not found.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<OperatorDatabase.Row> operator_databases = OperatorDatabase.getTable().selectRows(manager_connection, request_operator_name);
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
			if (!selected_operator_database.isWritable())
			{
				logger.warning("client=%s operator=%s database=%s : database is not writable.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<ManagedTable.Row> tables = ManagedTable.getTable().selectRows(manager_connection, request_database_name);
			if (!ManagedTable.getTable().hasRow(tables, request_table_name))
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not found.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<OperatorTable.Row> operator_tables = OperatorTable.getTable().selectRows(manager_connection, request_operator_name, request_database_name);
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
			if (!selected_operator_table.isWritable())
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not writable.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}

			for (final OperatorDatabase.Row operator_database : operator_databases)
			{
				if (operator_database.isSelected() && operator_database != selected_operator_database)
				{
					operator_database.isSelected(false);
					OperatorDatabase.getTable().updateRow(manager_connection, operator_database);
				}
			}
			if (!selected_operator_database.isSelected())
			{
				selected_operator_database.isSelected(true);
				OperatorDatabase.getTable().updateRow(manager_connection, selected_operator_database);
			}

			for (final OperatorTable.Row operator_table : operator_tables)
			{
				if (operator_table.isSelected() && operator_table != selected_operator_table)
				{
					operator_table.isSelected(false);
					OperatorTable.getTable().updateRow(manager_connection, operator_table);
				}
			}
			if (!selected_operator_table.isSelected())
			{
				selected_operator_table.isSelected(true);
				OperatorTable.getTable().updateRow(manager_connection, selected_operator_table);
			}

			final ManagedDatabase.Row database = ManagedDatabase.getTable().findRow(databases, request_database_name);
			final SqlConnection delete_connection = this.getConnection(database.getResource());
			try
			{
				final Array<InformationColumn.Row> columns = InformationColumn.getTable().selectRows(delete_connection, request_table_name);
				final Array<InformationKey.Row> keys = InformationKey.getTable().selectRows(delete_connection, request_table_name);
				final Hash<String,Data> primary_keys = Hash.newInstance();
				for (final InformationKey.Row key : keys)
				{
					if (key.isPrimary())
					{
						final String column_name = key.getColumnName();
						final String request_key = String.format("key:%s", column_name);
						if (request.hasParameter(request_key))
						{
							final Data request_value = request.getParameter(request_key);
							primary_keys.set(column_name, request_value);
						}
						else
						{
							primary_keys.set(column_name, DataFactory.getInstance().getNull());
						}
					}
				}

				Common.getInstance().deleteRow(delete_connection, request_table_name, columns, primary_keys);
			}
			catch (final RuntimeException cause)
			{
				delete_connection.rollback();
				throw cause;
			}
			finally
			{
				delete_connection.commit();
				delete_connection.close();
			}

			return new DatabaseViewProcess(this.getServer());
		}
		catch (final RuntimeException cause)
		{
			manager_connection.rollback();
			throw cause;
		}
		finally
		{
			manager_connection.commit();
			manager_connection.close();
		}
	}
}
