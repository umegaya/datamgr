/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.manager.core.data.Common;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.name.ResourceName;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;

/**
 * パスワード変更画面表示処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class PasswordViewProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public PasswordViewProcess(final HttpServer server)
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
	 * パスワード変更画面表示処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process view(final HttpServerRequest request)
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = request.getParameter("operator").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s : password view.", request_client, request_operator_name);
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

			Common.setUrlAttribute(request, logger);
			request.setAttribute("operator-name", request_operator_name);
			return new ForwardProcess(this.getServer(), ResourceName.PASSWORD_VIEW);
		}
		finally
		{
			connection.close();
		}
	}
}
