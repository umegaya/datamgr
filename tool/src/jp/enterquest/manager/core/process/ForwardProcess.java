/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;

/**
 * フォワード処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class ForwardProcess extends Process
{
	/** パス名 */
	private final String path;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 * @param path パス名
	 */
	public ForwardProcess(final HttpServer server, final String path)
	{
		super(server);
		this.path = path;
	}

	/**
	 * リクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	@Override
	public final void run(final HttpServerRequest request, final HttpServerResponse response)
	{
		request.forward(this.path);
	}
}
