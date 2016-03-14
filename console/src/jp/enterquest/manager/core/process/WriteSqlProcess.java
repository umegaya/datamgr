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
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.ConsoleLogger;
import jp.enterquest.manager.core.data.Common;
import jp.enterquest.manager.core.name.RequestName;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;

import java.sql.*;

/**
 * フォワード処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class WriteSqlProcess extends ExecSqlProcess
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 * @param path パス名
	 */
	public WriteSqlProcess(final HttpServer server)
	{
		super(server);
	}

	@Override
	public final void run(final HttpServerRequest request, final HttpServerResponse response)
	{
		try {
			final String stmt = java.net.URLDecoder.decode(request.getParameter("stmt").asString(), "UTF-8");
			ConsoleLogger.newInstance().debug("WriteSql:stmt=" + stmt);
			final SqlConnection connection = this.getConnection(this.getDbName());
			try{
				if (stmt != null) {
					//完全に開発用なので、sql injectionのことは考慮していない.
					final int ret = Common.getInstance().writeBySql(connection, stmt);
					response.setHeader("content-type", MimeType.APPLICATION_OCTETSTREAM.getName());
					final TextWriter text_writer = response.getStream().getTextWriter(CharacterEncoding.UTF_8, LineSeparator.LF);
					try
					{
						text_writer.write("" + ret);
						//System.out.println("" + ret);
					}
					finally
					{
						response.getStream().close();
					}
				}
			}
			finally
			{
				connection.commit();
				connection.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDbName()
	{
		return DatabaseName.GAME_WRITEABLE;
	}
}
