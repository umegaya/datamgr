/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.process;

import jp.enterquest.system.CharacterEncoding;
import jp.enterquest.system.Data;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.JsonEncoder;
import jp.enterquest.system.LineSeparator;
import jp.enterquest.system.MimeType;
import jp.enterquest.system.TextWriter;

/**
 * file出力処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class FileOutputProcess extends Process
{
	/** ファイル名 */
	private final String filename;
	/** format (json or csv) */
	private final String format;
	/** データ */
	private final Data data;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 * @param filename ファイル名
	 * @param data JSONデータ
	 */
	public FileOutputProcess(final HttpServer server, final String filename, final String format, final Data data)
	{
		super(server);
		this.filename = filename;
		this.data = data;
		this.format = format;
	}

	/**
	 * リクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	@Override
	public final void run(final HttpServerRequest request, final HttpServerResponse response)
	{
		response.setHeader("content-disposition", String.format("filename=\"%s\"", this.filename));
		response.setHeader("content-type", MimeType.APPLICATION_OCTETSTREAM.getName());
		final TextWriter text_writer = response.getStream().getTextWriter(CharacterEncoding.UTF_8, LineSeparator.LF);
		try
		{
			if (this.format.equals("json")) {
				text_writer.write(JsonEncoder.getInstance().encode(this.data));
			}
			else if (this.format.equals("csv")) {
				text_writer.write(CSVEncoder.getInstance().encode(this.data));
			}
		}
		finally
		{
			response.getStream().close();
		}
	}
}
