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
	HttpClientResponse resp;
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
		try
		{
			final HttpClient client = HttpClient.newInstance();
			this.url = this.getString(request, "url");
			this.method = this.getString(request, "method", "GET");
			client.setDelegate(this);
			client.connect();

			this.WriteResponse(response, this.resp.getStream());
		}
		catch (final RuntimeException cause)
		{
			throw cause;
		}
		finally
		{
		}
	}

	/**
	 * implements HttpClientDelegate
	 */
    public void onRequest(HttpClientRequest httpclientrequest) {
/*    	if (this.method.equals("GET")) {
	    	httpclientrequest.setMethod(HttpMethod.GET);
    	}
    	else if (this.method.equals("POST")) {
    		httpclientrequest.setMethod(HttpMethod.POST);
    	}*/
    	httpclientrequest.setUrl(this.url);
    }

    public void onResponse(HttpClientResponse httpclientresponse) {
    	this.resp = httpclientresponse;
    }
}
