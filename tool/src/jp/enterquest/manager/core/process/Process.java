/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.system.Database;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;

/**
 * リクエスト処理を提供する抽象クラス
 * @author Akinori Nishimura
 */
public abstract class Process
{
	/** HTTPサーバ */
	private final HttpServer server;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	protected Process(final HttpServer server)
	{
		this.server = server;
	}

	/**
	 * HTTPサーバを取得する
	 * @return HTTPサーバ
	 */
	protected final HttpServer getServer()
	{
		return this.server;
	}

	/**
	 * ロガーを取得する
	 * @param name ロガー名
	 * @return ロガー
	 */
	protected final Logger getLogger(final String name)
	{
		return this.getServer().getLoggers().get(name);
	}

	/**
	 * SQLコネクションを取得する
	 * @param name データベース名
	 * @return SQLコネクション
	 */
	protected final SqlConnection getConnection(final String name)
	{
		if (!this.getServer().getDatabases().has(name))
		{
			this.getServer().getDatabases().set(name, Database.newInstance(name));
		}
		final SqlConnection connection = this.getServer().getDatabases().get(name).getConnection();
		try
		{
			connection.isAutoCommit(false);
			return connection;
		}
		catch (final RuntimeException cause)
		{
			connection.close();
			throw cause;
		}
	}

	/**
	 * リクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	public abstract void run(HttpServerRequest request, HttpServerResponse response);
}
