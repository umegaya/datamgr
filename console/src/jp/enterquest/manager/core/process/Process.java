/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import java.io.*;
import jp.enterquest.system.Database;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.CharacterEncoding;
import jp.enterquest.system.LineSeparator;
import jp.enterquest.system.MimeType;
import jp.enterquest.system.Logger;
import jp.enterquest.system.ReaderStream;
import jp.enterquest.system.TextReader;
import jp.enterquest.system.TextWriter;
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
	 * 文字列を取得する
	 * @param request HTTPサーバリクエスト
	 * @param name リクエストパート名
	 * @return 文字列
	 */
	protected final String getString(final HttpServerRequest request, final String name) {
		return this.getString(request, name, null);
	}
	protected final String getString(final HttpServerRequest request, final String name, final String defaultValue)
	{
		if (!request.hasPart(name)) {
			return defaultValue;
		}
		final ReaderStream stream = request.getPart(name).getStream();
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

	protected final void WriteResponse(final HttpServerResponse response, final ReaderStream content) {
		response.setHeader("content-type", MimeType.APPLICATION_OCTETSTREAM.getName());
		final TextWriter text_writer = response.getStream().getTextWriter(CharacterEncoding.UTF_8, LineSeparator.LF);
		try
		{
			final TextReader reader = content.getTextReader(CharacterEncoding.UTF_8);
			int count = 0;
			while (reader.canRead()) {
				count++;
				text_writer.write(reader.readLine());
				if (count > 10000) {
					break;
				}
			}
		}
		finally
		{
			response.getStream().close();
		}
	}

	/**
	 * リクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	public abstract void run(HttpServerRequest request, HttpServerResponse response);
}
