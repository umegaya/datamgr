/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.manager.core.data.Menu;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorMenu;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.name.ResourceName;
import jp.enterquest.system.Array;
import jp.enterquest.system.CharacterEncoding;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.MimeType;
import jp.enterquest.system.ReaderStream;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.TextReader;

/**
 * 分岐処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class BranchProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public BranchProcess(final HttpServer server)
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
		final Process next_process = this.branch(request);
		next_process.run(request, response);
	}

	/**
	 * 分岐処理を取得する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process branch(final HttpServerRequest request)
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = this.getOperatorName(request);

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s : branching.", request_client, request_operator_name);
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

			final Array<Menu.Row> menus = Menu.getTable().selectRows(connection);
			final Array<OperatorMenu.Row> operator_menus = OperatorMenu.getTable().selectRows(connection, request_operator_name);

			if (!OperatorMenu.getTable().isAnySelected(operator_menus))
			{
				for (final OperatorMenu.Row operator_menu : operator_menus)
				{
					if (operator_menu.isSelected())
					{
						operator_menu.isSelected(false);
						OperatorMenu.getTable().updateRow(connection, operator_menu);
					}
				}
				for (final OperatorMenu.Row operator_menu : operator_menus)
				{
					if (operator_menu.isReadable())
					{
						operator_menu.isSelected(true);
						OperatorMenu.getTable().updateRow(connection, operator_menu);
						break;
					}
				}
			}

			for (final OperatorMenu.Row operator_menu : operator_menus)
			{
				if (operator_menu.isSelected() && operator_menu.isReadable())
				{
					final Menu.Row menu = Menu.getTable().findRow(menus, operator_menu.getMenu());
					request.setAttribute("url", request.getRequestUrl());
					return new ForwardProcess(this.getServer(), menu.getPath());
				}
			}

			request.setAttribute("url", request.getRequestUrl());
			request.setAttribute("operator-name", request_operator_name);
			request.setAttribute("menus", menus);
			request.setAttribute("operator-menus", operator_menus);
			return new ForwardProcess(this.getServer(), ResourceName.MENU_VIEW);
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

	/**
	 * オペレータ名を取得する
	 * @param request HTTPサーバリクエスト
	 * @return オペレータ名
	 */
	private final String getOperatorName(final HttpServerRequest request)
	{
		final String content_type = request.getHeader("content-type").asString().toLowerCase();
		if (content_type.equals(MimeType.APPLICATION_XWWWFORMURLENCODED.getName()))
		{
			return request.getParameter("operator").asString();
		}
		final ReaderStream stream = request.getPart("operator").getStream();
		try
		{
			final TextReader reader = stream.getTextReader(CharacterEncoding.UTF_8);
			return reader.readLine();
		}
		finally
		{
			stream.close();
		}
	}
}
