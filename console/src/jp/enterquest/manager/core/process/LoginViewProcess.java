/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.name.ResourceName;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;

/**
 * ログイン画面表示処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class LoginViewProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public LoginViewProcess(final HttpServer server)
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
	 * ログイン画面表示処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process view(final HttpServerRequest request)
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);

		final String request_client = request.getRemoteAddr();

		if (logger.isInfoEnabled())
		{
			logger.info("client=%s : login view.", request_client);
		}

		request.setAttribute("url", request.getRequestUrl());
		return new ForwardProcess(this.getServer(), ResourceName.LOGIN_VIEW);
	}
}
