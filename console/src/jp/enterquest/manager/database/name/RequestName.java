/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.name;

/**
 * リクエスト名を提供するクラス
 * @author Akinori Nishimura
 */
public final class RequestName
{
	/** データベース変更 */
	public static final String CHANGE_DATABASE = "request-change-database";
	/** テーブル変更 */
	public static final String CHANGE_TABLE = "request-change-table";
	/** レコード検索 */
	public static final String SELECT = "request-select";
	/** レコード追加 */
	public static final String INSERT = "request-insert";
	/** レコード更新 */
	public static final String UPDATE = "request-update";
	/** レコード削除 */
	public static final String DELETE = "request-delete";
	/** リナンバ */
	public static final String RENUMBER = "request-renumber";
	/** ダウンロード */
	public static final String DOWNLOAD = "request-download";
	/** アップロード */
	public static final String UPLOAD = "request-upload";
	/** SQL実行 */
	public static final String EXEC_SQL = "exec-sql";
	/** プロキシリクエスト */
	public static final String PROXY = "proxy-request";

	/**
	 * @deprecated
	 */
	@Deprecated
	private RequestName()
	{
	}
}
