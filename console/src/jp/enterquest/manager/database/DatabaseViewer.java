/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database;

import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.name.ParameterName;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.manager.database.name.RequestName;
import jp.enterquest.manager.database.process.ChangeDatabaseProcess;
import jp.enterquest.manager.database.process.ChangeTableProcess;
import jp.enterquest.manager.database.process.DatabaseViewProcess;
import jp.enterquest.manager.database.process.DeleteProcess;
import jp.enterquest.manager.database.process.DownloadProcess;
import jp.enterquest.manager.database.process.InsertProcess;
import jp.enterquest.manager.database.process.RenumberProcess;
import jp.enterquest.manager.database.process.SelectProcess;
import jp.enterquest.manager.database.process.UpdateProcess;
import jp.enterquest.manager.database.process.UploadProcess;
import jp.enterquest.manager.database.process.ProxyProcess;
import jp.enterquest.system.ConsoleLogger;
import jp.enterquest.system.Data;
import jp.enterquest.system.Database;
import jp.enterquest.system.FileLogger;
import jp.enterquest.system.Hash;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerDelegate;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.LogType;
import jp.enterquest.system.Logger;
import jp.enterquest.system.MimeType;
import jp.enterquest.system.SyslogLogger;

/**
 * データベースビューアを提供するクラス
 * @author Akinori Nishimura
 */
public final class DatabaseViewer implements HttpServerDelegate
{
	/** HTTPサーバ */
	private final HttpServer server;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public DatabaseViewer(final HttpServer server)
	{
		this.server = server;
	}

	/**
	 * HTTPサーバを取得する
	 * @return HTTPサーバ
	 */
	@Override
	public final HttpServer getServer()
	{
		return this.server;
	}

	/**
	 * 初期化処理を実行する
	 */
	@Override
	public final void onInitialize()
	{
		final Logger logger = this.newLogger();
		try
		{
			logger.info("start");

			// ロガー登録
			this.getServer().getLoggers().set(LoggerName.OPTION, logger);

			// データベース登録
			this.getServer().getDatabases().set(DatabaseName.MANAGER, Database.newInstance(DatabaseName.MANAGER));
		}
		catch (final RuntimeException cause)
		{
			logger.error(cause);
		}
		finally
		{
			logger.info("end");
		}
	}

	/**
	 * 終了処理を実行する
	 */
	@Override
	public final void onFinalize()
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		try
		{
			logger.info("start");
		}
		catch (final RuntimeException cause)
		{
			logger.error(cause);
		}
		finally
		{
			logger.info("end");
		}
	}

	/**
	 * GETリクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	@Override
	public final void onGet(final HttpServerRequest request, final HttpServerResponse response)
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		try
		{
			logger.debug("start");

			final String client_address = request.getRemoteAddr();
			logger.warning("client=%s : access denied.", client_address);
		}
		catch (final RuntimeException cause)
		{
			logger.error(cause);
		}
		finally
		{
			logger.debug("end");
		}
	}

	/**
	 * POSTリクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	@Override
	public final void onPost(final HttpServerRequest request, final HttpServerResponse response)
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		try
		{
			if (!request.hasAttribute("url"))
			{
				final String client_address = request.getRemoteAddr();
				logger.warning("client=%s : access denied.", client_address);
				return;
			}

			final Process process = this.newProcess(request);
			process.run(request, response);
		}
		catch (final RuntimeException cause)
		{
			logger.error(cause);
		}
		finally
		{
		}
	}

	/**
	 * プロセスを生成する
	 * @param request HTTPサーバリクエスト
	 * @return プロセス
	 */
	private final Process newProcess(final HttpServerRequest request)
	{
		final String content_type = request.getHeader("content-type").asString().toLowerCase();
		if (content_type.equals(MimeType.APPLICATION_XWWWFORMURLENCODED.getName()))
		{
			if (request.hasParameter(RequestName.CHANGE_DATABASE))
			{
				return new ChangeDatabaseProcess(this.server);
			}
			if (request.hasParameter(RequestName.CHANGE_TABLE))
			{
				return new ChangeTableProcess(this.server);
			}
			if (request.hasParameter(RequestName.SELECT))
			{
				return new SelectProcess(this.server);
			}
			if (request.hasParameter(RequestName.INSERT))
			{
				return new InsertProcess(this.server);
			}
			if (request.hasParameter(RequestName.UPDATE))
			{
				return new UpdateProcess(this.server);
			}
			if (request.hasParameter(RequestName.DELETE))
			{
				return new DeleteProcess(this.server);
			}
			if (request.hasParameter(RequestName.RENUMBER))
			{
				return new RenumberProcess(this.server);
			}
			if (request.hasParameter(RequestName.DOWNLOAD))
			{
				return new DownloadProcess(this.server);
			}
			if (request.hasParameter(RequestName.PROXY))
			{
				return new ProxyProcess(this.server);
			}
		}
		else if (content_type.startsWith(MimeType.MULTIPART_FORMDATA.getName()))
		{
			if (request.hasPart(RequestName.UPLOAD))
			{
				return new UploadProcess(this.server);
			}
		}
		return new DatabaseViewProcess(this.server);
	}

	/**
	 * ロガーを生成する
	 * @return ロガー
	 */
	private final Logger newLogger()
	{
		final Hash<String,Data> parameters = this.getServer().getParameters();
		final String log_type = parameters.get(ParameterName.LOG_TYPE).asString();
		if (log_type.equals(LogType.CONSOLE.getName()))
		{
			return ConsoleLogger.newInstance();
		}
		else if (log_type.equals(LogType.FILE.getName()))
		{
			final String filename = parameters.get(ParameterName.LOG_FILE).asString();
			final int history = parameters.get(ParameterName.LOG_HISTORY).asInt32();
			return FileLogger.newInstance(filename, history);
		}
		else if (log_type.equals(LogType.SYSLOG.getName()))
		{
			final String hostname = parameters.get(ParameterName.LOG_HOST).asString();
			final String facility = parameters.get(ParameterName.LOG_FACILITY).asString();
			return SyslogLogger.newInstance(hostname, facility, this.getServer().getName());
		}
		return null;
	}

	/**
	 * ロガーを取得する
	 * @param name ロガー名
	 * @return ロガー
	 */
	private final Logger getLogger(final String name)
	{
		return this.getServer().getLoggers().get(name);
	}
}
