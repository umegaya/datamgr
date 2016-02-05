/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.name;

/**
 * リクエスト名を提供するクラス
 * @author Akinori Nishimura
 */
public final class RequestName
{
	/** ログイン */
	public static final String LOGIN = "request-login";
	/** ログアウト */
	public static final String LOGOUT = "request-logout";
	/** メニュー変更 */
	public static final String CHANGE_MENU = "request-change-menu";
	/** パスワード変更画面 */
	public static final String PASSWORD_VIEW = "request-password-view";
	/** パスワード変更 */
	public static final String CHANGE_PASSWORD = "request-change-password";

	/**
	 * @deprecated
	 */
	@Deprecated
	private RequestName()
	{
	}
}
