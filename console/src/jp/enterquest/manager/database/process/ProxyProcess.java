/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.process;

import java.io.*;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.system.Array;
import jp.enterquest.system.Data;
import jp.enterquest.system.DataFactory;
import jp.enterquest.system.Hash;
import jp.enterquest.system.HtmlDecoder;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.HttpClient;
import jp.enterquest.system.HttpClientRequest;
import jp.enterquest.system.HttpClientResponse;
import jp.enterquest.system.HttpClientDelegate;
import jp.enterquest.system.ReaderStream;
import jp.enterquest.system.Logger;

/**
 * 別のところへのリクエストを中継する クラス
 * @author Akinori Nishimura
 */
public final class ProxyProcess extends Process implements HttpClientDelegate
{
	String url;
	String method;
	HttpServerResponse resp;
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public ProxyProcess(final HttpServer server)
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
		final Logger logger = this.getLogger(LoggerName.OPTION);
		final String request_client = request.getRemoteAddr();
		final String request_operator_name = this.getOperatorName(request);
		try
		{
			final HttpClient client = HttpClient.newInstance();
			this.url = request.getParameter("url").asString();
			logger.info("client=%s operator=%s : requests %s.", request_client, request_operator_name, this.url);
			this.resp = response;
			client.setDelegate(this);
			client.connect();
		}
		catch (final Exception cause)
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			cause.printStackTrace(pw);
			pw.flush();
			String str = sw.toString();
			logger.info("proxy request error:%s", str);
			this.WriteResponse(this.resp, cause.getMessage());
		}
		finally
		{
		}
	}

	/**
	 * implements HttpClientDelegate
	 */
    public void onRequest(HttpClientRequest req) {
    	req.setUrl(this.url);
    }

    public void onResponse(HttpClientResponse resp) {
		this.WriteResponse(this.resp, resp.getStream());
    }
}
