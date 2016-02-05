/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.name;

/**
 * リソース名を提供するクラス
 * @author Akinori Nishimura
 */
public final class ResourceName
{
	/** ログイン画面 */
	public static final String LOGIN_VIEW = "/WEB-INF/jsp/login-view.jsp";
	/** パスワード変更画面 */
	public static final String PASSWORD_VIEW = "/WEB-INF/jsp/password-view.jsp";
	/** メニュー画面 */
	public static final String MENU_VIEW = "/WEB-INF/jsp/menu-view.jsp";

	/**
	 * @deprecated
	 */
	@Deprecated
	private ResourceName()
	{
	}
}
