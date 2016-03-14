/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.name;

/**
 * データベース名を提供するクラス
 * @author Akinori Nishimura
 */
public final class DatabaseName
{
	/** 管理ツールデータベース */
	public static final String MANAGER = "jdbc/manager";
	/** ゲームデータベース */
	public static final String GAME = "jdbc/game-readonly";

	public static final String GAME_WRITEABLE = "jdbc/game-readwrite";

	/**
	 * @deprecated
	 */
	@Deprecated
	private DatabaseName()
	{
	}
}
