/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.name;

/**
 * パラメータ名を提供するクラス
 * @author Akinori Nishimura
 */
public final class ParameterName
{
	/** 許可IPアドレス */
	public static final String ALLOWED_ADDRESSES = "allowed-addresses";
	/** ログタイプ */
	public static final String LOG_TYPE = "log-type";
	/** ログファイル名 */
	public static final String LOG_FILE = "log-file";
	/** ログ保存日数 */
	public static final String LOG_HISTORY = "log-history";
	/** ログホスト名 */
	public static final String LOG_HOST = "log-host";
	/** ログファシリティ名 */
	public static final String LOG_FACILITY = "log-facility";

	/**
	 * @deprecated
	 */
	@Deprecated
	private ParameterName()
	{
	}
}
