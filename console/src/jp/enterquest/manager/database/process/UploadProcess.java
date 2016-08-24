/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.database.process;

import jp.enterquest.manager.core.data.Common;
import jp.enterquest.manager.core.data.InformationColumn;
import jp.enterquest.manager.core.data.ManagedDatabase;
import jp.enterquest.manager.core.data.ManagedTable;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorDatabase;
import jp.enterquest.manager.core.data.OperatorTable;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.manager.core.process.LoginViewProcess;
import jp.enterquest.manager.core.process.Process;
import jp.enterquest.manager.core.process.CSVDecoder;
import jp.enterquest.system.Array;
import jp.enterquest.system.BinaryReader;
import jp.enterquest.system.CharacterEncoding;
import jp.enterquest.system.Data;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.HttpServerRequest;
import jp.enterquest.system.HttpServerResponse;
import jp.enterquest.system.JsonDecoder;
import jp.enterquest.system.Logger;
import jp.enterquest.system.ReaderStream;
import jp.enterquest.system.SqlConnection;
import jp.enterquest.system.TextReader;
import jp.enterquest.system.DataFactory;
import jp.enterquest.system.Random;
import java.util.HashMap;

/**
 * アップロード処理を提供するクラス
 * @author Akinori Nishimura
 */
public final class UploadProcess extends Process
{
	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public UploadProcess(final HttpServer server)
	{
		super(server);
	}

	/**
	 * リクエスト処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @param response HTTPサーバレスポンス
	 */
	@Override
	public final void run(final HttpServerRequest request, final HttpServerResponse response)
	{
		final Process next_process = this.upload(request);
		next_process.run(request, response);
	}

	/**
	 * アップロード処理を実行する
	 * @param request HTTPサーバリクエスト
	 * @return 次の処理
	 */
	private final Process upload(final HttpServerRequest request)
	{
		final SqlConnection manager_connection = this.getConnection(DatabaseName.MANAGER);
		final Logger logger = this.getLogger(LoggerName.OPTION);
		try
		{
			final String request_client = request.getRemoteAddr();
			final String request_operator_name = this.getString(request, "operator");
			final String request_database_name = this.getString(request, "database");
			final String request_table_name = this.getString(request, "table");
			final Data rows = this.getJson(request, "file", "format");

			logger.info("client=%s operator=%s database=%s table=%s : upload file.", request_client, request_operator_name, request_database_name, request_table_name);

			if (!Operator.getTable().existsRow(manager_connection, request_operator_name))
			{
				logger.warning("client=%s operator=%s : operator is not found.", request_client, request_operator_name);
				return new LoginViewProcess(this.getServer());
			}
			final Operator.Row operator = Operator.getTable().selectRow(manager_connection, request_operator_name);
			if (!operator.isPublished())
			{
				logger.warning("client=%s operator=%s : operator is not published.", request_client, request_operator_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(manager_connection);
			if (!ManagedDatabase.getTable().hasRow(databases, request_database_name))
			{
				logger.warning("client=%s operator=%s database=%s : database is not found.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<OperatorDatabase.Row> operator_databases = OperatorDatabase.getTable().selectRows(manager_connection, request_operator_name);
			if (!OperatorDatabase.getTable().hasRow(operator_databases, request_operator_name, request_database_name))
			{
				logger.warning("client=%s operator=%s database=%s : operator database is not found.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final OperatorDatabase.Row selected_operator_database = OperatorDatabase.getTable().findRow(operator_databases, request_database_name);
			if (!selected_operator_database.isReadable())
			{
				logger.warning("client=%s operator=%s database=%s : database is not readable.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			if (!selected_operator_database.isWritable())
			{
				logger.warning("client=%s operator=%s database=%s : database is not writable.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			if (!selected_operator_database.isUploadable())
			{
				logger.warning("client=%s operator=%s database=%s : database is not uploadable.", request_client, request_operator_name, request_database_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<ManagedTable.Row> tables = ManagedTable.getTable().selectRows(manager_connection, request_database_name);
			if (!ManagedTable.getTable().hasRow(tables, request_table_name))
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not found.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			final Array<OperatorTable.Row> operator_tables = OperatorTable.getTable().selectRows(manager_connection, request_operator_name, request_database_name);
			if (!OperatorTable.getTable().hasRow(operator_tables, request_table_name))
			{
				logger.warning("client=%s operator=%s database=%s table=%s : operator table is not found.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			final OperatorTable.Row selected_operator_table = OperatorTable.getTable().findRow(operator_tables, request_table_name);
			if (!selected_operator_table.isReadable())
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not readable.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			if (!selected_operator_table.isWritable())
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not writable.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}
			if (!selected_operator_table.isUploadable())
			{
				logger.warning("client=%s operator=%s database=%s table=%s : table is not uploadable.", request_client, request_operator_name, request_database_name, request_table_name);
				return new LoginViewProcess(this.getServer());
			}

			for (final OperatorDatabase.Row operator_database : operator_databases)
			{
				if (operator_database.isSelected() && operator_database != selected_operator_database)
				{
					operator_database.isSelected(false);
					OperatorDatabase.getTable().updateRow(manager_connection, operator_database);
				}
			}
			if (!selected_operator_database.isSelected())
			{
				selected_operator_database.isSelected(true);
				OperatorDatabase.getTable().updateRow(manager_connection, selected_operator_database);
			}

			for (final OperatorTable.Row operator_table : operator_tables)
			{
				if (operator_table.isSelected() && operator_table != selected_operator_table)
				{
					operator_table.isSelected(false);
					OperatorTable.getTable().updateRow(manager_connection, operator_table);
				}
			}
			if (!selected_operator_table.isSelected())
			{
				selected_operator_table.isSelected(true);
				OperatorTable.getTable().updateRow(manager_connection, selected_operator_table);
			}

			final ManagedDatabase.Row database = ManagedDatabase.getTable().findRow(databases, request_database_name);
			final SqlConnection upload_connection = this.getConnection(database.getResource());
			try
			{
				final ManagedTable.Row table = ManagedTable.getTable().findRow(tables, request_table_name);
				final Array<InformationColumn.Row> columns = InformationColumn.getTable().selectRows(upload_connection, request_table_name);
				// idカラムがある場合はCSV(JSON)で利用されているidを収集してid群のデータを作成して
				// ランダムidを生成する際に利用済みかのチェックで使用する
				final HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();
				if(true == InformationColumn.getTable().existsRow(upload_connection, request_table_name, "id"))
				{
					for (final Data row : rows.asArray())
					{
						ids.put(row.asHash().get("id").asInt32(), 1);
					}
				}

				Common.getInstance().deleteRows(upload_connection, request_table_name);
				for (final Data row : rows.asArray())
				{
					// idカラムが存在していて、且つデータが設定されていない場合はランダムな数値を生成し強制的に設定する　
					if((true == InformationColumn.getTable().existsRow(upload_connection, request_table_name, "id")) && (0 == row.asHash().get("id").asInt32()))
					{
						final Data value = this.getRandomValue(ids);
						row.asHash().set("id", value);
					}
				/*logger.warning("row = %d: created = [%s] %s", 
					row.asHash().has("id") ? row.asHash().get("id").asInt32() : -1, 
					row.asHash().has("created") ? row.asHash().get("created").asString() : "Null", 
					InformationColumn.getTable().existsRow(upload_connection, request_table_name, "id") ? "id-exists" : "no-id"); */

					Common.getInstance().insertRow(upload_connection, table, columns, row.asHash(), logger);
				}
			}
			catch (final RuntimeException cause)
			{
				upload_connection.rollback();
				throw cause;
			}
			finally
			{
				upload_connection.commit();
				upload_connection.close();
			}

			return new DatabaseViewProcess(this.getServer());
		}
		catch (final RuntimeException cause)
		{
		logger.info("upload error:" + cause.getMessage());
			manager_connection.rollback();
			throw cause;
		}
		finally
		{
			manager_connection.commit();
			manager_connection.close();
		}
	}

	/**
	 * JSONデータを取得する
	 * @param request HTTPサーバリクエスト
	 * @param name リクエストパート名
	 * @return JSONデータ
	 */
	private final Data getJson(final HttpServerRequest request, final String name, final String format_name)
	{
		final ReaderStream stream = request.getPart(name).getStream();
		final Logger logger = this.getLogger(LoggerName.OPTION);
		final String format = this.getString(request, format_name);
		try
		{
			final BinaryReader reader = stream.getBinaryReader();
			final byte[] bytes = reader.readFully(10240);
			final String text = new String(bytes, CharacterEncoding.UTF_8.getName());
			if (format.equals("json")) {
				return JsonDecoder.getInstance().decode(text);
			}
			else if (format.equals("csv")) {
				return CSVDecoder.getInstance().decode(text, logger);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		catch (final java.io.UnsupportedEncodingException cause)
		{
			throw new RuntimeException(cause);
		}
		finally
		{
			stream.close();
		}
	}

	/**
	 * CSV(JSON)で利用していないランダムなIDを取得する
	 * @param ids CSV(JSON)で利用しているID郡
	 * @return ランダムな値
	 */
	private final Data getRandomValue(final HashMap<Integer, Integer> ids)
	{
		final Random random = Random.newInstance();
		final DataFactory factory = DataFactory.getInstance();

		// ランダムidの生成を100回まで試みる
		for(int loop = 0; loop < 100; loop++){
			final Data value = factory.newInt32(random.nextInt32() % 10000000 + 10000000);

			// ランダムidがid群で既に利用されているか確認する
			if(null == ids.get(value.asInt32())){
				// 利用済みidは再度使われないようid群に追加する
				ids.put(value.asInt32(), 1);

				return value;
			}
		}

		// ここまで実行されてしまった場合は100回連続でランダム値の生成に失敗したので終了させる
		throw new RuntimeException("random value is not generated.");
	}
}
