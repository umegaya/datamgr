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
 * パスワード変更処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class ChangePasswordProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public ChangePasswordProcess(final HttpServer server)
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
		final Process next_process = this.changePassword(request);
		next_process.run(request, response);
	}

	/**
	 * パスワード変更処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process changePassword(final HttpServerRequest request)
	{
		final Md5Encoder md5_encoder = Md5Encoder.getInstance();
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Logger logger = this.getLogger(LoggerName.OPTION);

			final String request_client = request.getRemoteAddr();
			final String request_operator_name = request.getParameter("operator").asString();
			final String request_password = request.getParameter("password").asString();
			final String request_new_password = request.getParameter("new-password").asString();
			final String request_new_password_confirmation = request.getParameter("new-password-confirmation").asString();

			if (logger.isInfoEnabled())
			{
				logger.info("client=%s operator=%s : change password.", request_client, request_operator_name);
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
			final String encoded_password = md5_encoder.encode(request_password, CharacterEncoding.UTF_8);
			if (!operator.getPassword().equals(encoded_password))
			{
				logger.warning("client=%s operator=%s : password is not correct.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new PasswordViewProcess(this.getServer());
			}
			if (request_new_password.isEmpty())
			{
				logger.warning("client=%s operator=%s : new password is empty.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new PasswordViewProcess(this.getServer());
			}
			if (!request_new_password.equals(request_new_password_confirmation))
			{
				logger.warning("client=%s operator=%s : new password is not correct.", request_client, request_operator_name);
				request.setAttribute("has-error", true);
				return new PasswordViewProcess(this.getServer());
			}

			final String encoded_new_password = md5_encoder.encode(request_new_password, CharacterEncoding.UTF_8);
			operator.setPassword(encoded_new_password);
			Operator.getTable().updateRow(connection, operator);

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
