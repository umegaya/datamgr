/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.manager.core.data.Menu;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorMenu;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.system.Array;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;

/**
 * メニュー変更処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class ChangeMenuProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public ChangeMenuProcess(final HttpServer server)
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
		final Process next_process = this.changeMenu(request);
		next_process.run(request, response);
	}

	/**
	 * メニュー変更処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process changeMenu(final HttpServerRequest request)
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = request.getParameter("operator").asString();
			final String request_menu_name = request.getParameter("menu").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s menu=%s : change menu.", request_client, request_operator_name, request_menu_name);
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

			if (!Menu.getTable().existsRow(connection, request_menu_name))
			{
				logger.warning("client=%s operator=%s menu=%s : menu is not found.", request_client, request_operator_name, request_menu_name);
				return new LoginViewProcess(this.getServer());
			}

			final Array<OperatorMenu.Row> operator_menus = OperatorMenu.getTable().selectRows(connection, request_operator_name);
			if (!OperatorMenu.getTable().hasRow(operator_menus, request_menu_name))
			{
				logger.warning("client=%s operator=%s menu=%s : operator menu is not found.", request_client, request_operator_name, request_menu_name);
				return new LoginViewProcess(this.getServer());
			}
			final OperatorMenu.Row selected_operator_menu = OperatorMenu.getTable().findRow(operator_menus, request_menu_name);
			if (!selected_operator_menu.isReadable())
			{
				logger.warning("client=%s operator=%s menu=%s : operator menu is not readable.", request_client, request_operator_name, request_menu_name);
				return new LoginViewProcess(this.getServer());
			}

			for (final OperatorMenu.Row operator_menu : operator_menus)
			{
				if (operator_menu.isSelected() && operator_menu != selected_operator_menu)
				{
					operator_menu.isSelected(false);
					OperatorMenu.getTable().updateRow(connection, operator_menu);
				}
			}
			if (!selected_operator_menu.isSelected())
			{
				selected_operator_menu.isSelected(true);
				OperatorMenu.getTable().updateRow(connection, selected_operator_menu);
			}

			return new BranchProcess(this.getServer());
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
