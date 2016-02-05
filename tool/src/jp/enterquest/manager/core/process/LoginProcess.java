/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.system.CharacterEncoding;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.Md5Encoder;
import jp.enterquest.system.SqlConnection;

/**
 * ログイン処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class LoginProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public LoginProcess(final HttpServer server)
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
		final Process next_process = this.login(request);
		next_process.run(request, response);
	}

	/**
	 * ログイン処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process login(final HttpServerRequest request)
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = request.getParameter("operator").asString();
			final String request_password = request.getParameter("password").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s : login.", request_client, request_operator_name);
			}

			if (request_operator_name.isEmpty())
			{
				logger.warning("client=%s operator=%s : name is empty.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new LoginViewProcess(this.getServer());
			}
			if (request_password.isEmpty())
			{
				logger.warning("client=%s operator=%s : password is empty.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new LoginViewProcess(this.getServer());
			}
			if (!Operator.getTable().existsRow(connection, request_operator_name))
			{
				logger.warning("client=%s operator=%s : operator is not found.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new LoginViewProcess(this.getServer());
			}
			final Operator.Row operator = Operator.getTable().selectRow(connection, request_operator_name);
			if (!operator.isPublished())
			{
				logger.warning("client=%s operator=%s : operator is not published.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new LoginViewProcess(this.getServer());
			}
			final String encoded_password = Md5Encoder.getInstance().encode(request_password, CharacterEncoding.UTF_8);
			if (!operator.getPassword().equals(encoded_password))
			{
				logger.warning("client=%s operator=%s : password is not correct.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new LoginViewProcess(this.getServer());
			}

			return new BranchProcess(this.getServer());
		}
		finally
		{
			connection.close();
		}
	}
}
