/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.task;

import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.system.Database;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.Timer;
import jp.enterquest.system.TimerDelegate;

/**
 * タスクを提供する抽象クラス
 * @author Akinori Nishimura
 */
public abstract class Task implements TimerDelegate
{
	/** HTTPサーバ */
	private final HttpServer server;
	/** タイマー */
	private final Timer timer;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 * @param interval タイマー実行間隔[単位:ミリ秒]
	 */
	protected Task(final HttpServer server, final long interval)
	{
		this.server = server;
		this.timer = Timer.newInstance();
		this.execute();
		this.timer.getDelegates().add(this);
		this.timer.start(interval, true);
	}

	/**
	 * タイマーを取得する
	 * @return タイマー
	 */
	@Override
	public final Timer getTimer()
	{
		return this.timer;
	}

	/**
	 * タイマー処理を実行する
	 * @param timer タイマー
	 */
	@Override
	public final void onTick(final Timer timer)
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		try
		{
			logger.debug("start");
			this.execute();
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
	 * タスク処理を実行する
	 */
	protected abstract void execute();
}
