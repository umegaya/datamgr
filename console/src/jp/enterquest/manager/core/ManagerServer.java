/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core;

import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.name.ParameterName;
import jp.enterquest.manager.core.name.RequestName;
import jp.enterquest.manager.core.name.TimerName;
import jp.enterquest.manager.core.process.BranchProcess;
import jp.enterquest.manager.core.process.ChangeMenuProcess;
import jp.enterquest.manager.core.process.ChangePasswordProcess;
import jp.enterquest.manager.core.process.LoginProcess;
import jp.enterquest.manager.core.process.LoginViewProcess;
import jp.enterquest.manager.core.process.PasswordViewProcess;
import jp.enterquest.manager.core.process.ExecSqlProcess;
import jp.enterquest.manager.core.process.WriteSqlProcess;
import jp.enterquest.manager.core.process.WriteSqlProcess;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.manager.core.task.SynchronizationTask;
import jp.enterquest.system.CharacterEncoding;
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
import jp.enterquest.system.ConsoleLogger;
import jp.enterquest.system.MimeType;
import jp.enterquest.system.SyslogLogger;

/**
 * 管理ツールサーバを提供するクラス
 * @author Akinori Nishimura
 */
public final class ManagerServer implements HttpServerDelegate
{
	/** HTTPサーバ */
	private final HttpServer server;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public ManagerServer(final HttpServer server)
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

			// タイマー登録
			this.getServer().getTimers().set(TimerName.SYNCHRONIZATION, new SynchronizationTask(this.getServer()).getTimer());
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

			request.setEncoding(CharacterEncoding.UTF_8);

			final String client_address = request.getRemoteAddr();
			if (!this.isAllowed(client_address))
			{
				logger.warning("client=%s : access denied.", client_address);
				return;
			}

			final Process process = new LoginViewProcess(this.server);
			process.run(request, response);
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
			request.setEncoding(CharacterEncoding.UTF_8);

			final String client_address = request.getRemoteAddr();
			if (!this.isAllowed(client_address))
			{
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
			if (request.hasParameter(RequestName.LOGIN))
			{
				return new LoginProcess(this.server);
			}
			if (request.hasParameter(RequestName.LOGOUT))
			{
				return new LoginViewProcess(this.server);
			}
			if (request.hasParameter(RequestName.PASSWORD_VIEW))
			{
				return new PasswordViewProcess(this.server);
			}
			if (request.hasParameter(RequestName.CHANGE_PASSWORD))
			{
				return new ChangePasswordProcess(this.server);
			}
			if (request.hasParameter(RequestName.CHANGE_MENU))
			{
				return new ChangeMenuProcess(this.server);
			}
		}
		//以降はdebugメニュー.
		final String isDevelopStr = this.server.getServletContext().getInitParameter("isDevelop");
		if (isDevelopStr.equals("true")) {
			if (request.hasParameter("exec-sql"))
			{
				return new ExecSqlProcess(this.server);
			}
			else if (request.hasParameter("write-sql"))
			{
				return new WriteSqlProcess(this.server);
			}
		}
		return new BranchProcess(this.server);
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

	/**
	 * クライアントのIPアドレスを許可するかどうかを取得する
	 * @param client_address クライアントのIPアドレス
	 * @return クライアントのIPアドレスを許可する場合はtrueを返す
	 */
	private final boolean isAllowed(final String client_address)
	{
		final String allow_addresses = this.getServer().getParameters().get(ParameterName.ALLOWED_ADDRESSES).asString();
		final int client_bits = this.getBitsFromAddress(client_address);
		if (allow_addresses.isEmpty()) {
			return true;
		}
		for (final String allow_address : allow_addresses.split(" "))
		{
			final String[] array = allow_address.split("/");
			final int allow_bits = this.getBitsFromAddress(array[0]);
			final int subnet_mask = this.getBitsFromSubnetMask((2 <= array.length) ? (Integer.parseInt(array[1])) : (32));
			if ((client_bits & subnet_mask) == (allow_bits & subnet_mask))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * IPアドレスのビット表現を取得する
	 * @param address IPアドレスの文字列表現
	 * @return IPアドレスのビット表現
	 */
	private final int getBitsFromAddress(final String address)
	{
		final String[] array = address.split("\\.");
		final int _0 = Integer.parseInt(array[0]) & 0xff;
		final int _1 = Integer.parseInt(array[1]) & 0xff;
		final int _2 = Integer.parseInt(array[2]) & 0xff;
		final int _3 = Integer.parseInt(array[3]) & 0xff;
		return (_0 << 24) | (_1 << 16) | (_2 << 8) | _3;
	}

	/**
	 * サブネットマスクのビット表現を取得する
	 * @param subnet_mask サブネットマスク値
	 * @return サブネットマスクのビット表現
	 */
	private final int getBitsFromSubnetMask(final int subnet_mask)
	{
		return (0xffffffff << (32 - subnet_mask));
	}
}
