/*
 * Copyright © Enterquest Inc. All right reserved.
 */
package jp.enterquest.manager.core.task;

import jp.enterquest.manager.core.data.InformationColumn;
import jp.enterquest.manager.core.data.InformationTable;
import jp.enterquest.manager.core.data.ManagedColumn;
import jp.enterquest.manager.core.data.ManagedDatabase;
import jp.enterquest.manager.core.data.ManagedTable;
import jp.enterquest.manager.core.data.Menu;
import jp.enterquest.manager.core.data.Operator;
import jp.enterquest.manager.core.data.OperatorColumn;
import jp.enterquest.manager.core.data.OperatorDatabase;
import jp.enterquest.manager.core.data.OperatorMenu;
import jp.enterquest.manager.core.data.OperatorTable;
import jp.enterquest.manager.core.data.Relation;
import jp.enterquest.manager.core.data.RelationRule;
import jp.enterquest.manager.core.name.DatabaseName;
import jp.enterquest.manager.core.name.LoggerName;
import jp.enterquest.system.Array;
import jp.enterquest.system.Hash;
import jp.enterquest.system.HttpServer;
import jp.enterquest.system.Logger;
import jp.enterquest.system.SqlConnection;

/**
 * 同期タスクを提供するクラス
 * @author Akinori Nishimura
 */
public final class SynchronizationTask extends Task
{
	/** タイマー実行間隔[単位:ミリ秒] */
	private static final long INTERVAL = 1 * 60 * 1000;

	/**
	 * コンストラクタ
	 * @param server HTTPサーバ
	 */
	public SynchronizationTask(final HttpServer server)
	{
		super(server, SynchronizationTask.INTERVAL);
	}

	/**
	 * タスク処理を実行する
	 */
	@Override
	public final void execute()
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		try
		{
			logger.info("start");

			// テーブル情報同期
			this.synchronizeTable();
			// カラム情報同期
			this.synchronizeColumn();
			// リレーション情報同期
			this.synchronizeRelation();
			// オペレータ別メニュー情報同期
			this.synchronizeOperatorMenu();
			// オペレータ別データベース情報同期
			this.synchronizeOperatorDatabase();
			// オペレータ別テーブル情報同期
			this.synchronizeOperatorTable();
			// オペレータ別カラム情報同期
			this.synchronizeOperatorColumn();
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
	 * テーブル情報を同期する
	 */
	private final void synchronizeTable()
	{
		final SqlConnection manager_connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(manager_connection);
			final Array<ManagedTable.Row> tables = ManagedTable.getTable().selectRows(manager_connection);
			final Hash<String,Array<InformationTable.Row>> database_tables = Hash.newInstance();
			for (final ManagedDatabase.Row database : databases)
			{
				final SqlConnection connection = this.getConnection(database.getResource());
				try
				{
					final String database_name = database.getName();
					final Array<InformationTable.Row> information_tables = InformationTable.getTable().selectRows(connection);
					database_tables.set(database_name, information_tables);
				}
				finally
				{
					connection.close();
				}
			}

			// もったいないのでキャッシュしておく.
			ManagedTable.setCache(tables);

			// テーブル情報追加
			for (final ManagedDatabase.Row database : databases)
			{
				final String database_name = database.getName();
				final Array<InformationTable.Row> information_tables = database_tables.get(database_name);
				for (final InformationTable.Row information_table : information_tables)
				{
					final String table_name = information_table.getTableName();
					if (!ManagedTable.getTable().hasRow(tables, database_name, table_name))
					{
						ManagedTable.getTable().insertRow(manager_connection, database_name, table_name);
					}
				}
			}
			// テーブル情報削除
			for (final ManagedTable.Row table : tables)
			{
				final String database_name = table.getDatabase();
				final String table_name = table.getName();
				if (!database_tables.has(database_name))
				{
					ManagedTable.getTable().deleteRow(manager_connection, database_name, table_name);
				}
				else
				{
					final Array<InformationTable.Row> information_tables = database_tables.get(database_name);
					if (!InformationTable.getTable().hasRow(information_tables, table_name))
					{
						ManagedTable.getTable().deleteRow(manager_connection, database_name, table_name);
					}
				}
			}
		}
		catch (final RuntimeException cause)
		{
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
	 * カラム情報を同期する
	 */
	private final void synchronizeColumn()
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		final SqlConnection manager_connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(manager_connection);
			final Array<ManagedColumn.Row> columns = ManagedColumn.getTable().selectRows(manager_connection);
			final Hash<String,Array<InformationColumn.Row>> database_columns = Hash.newInstance();
			final Hash<String,Array<InformationTable.Row>> database_tables = Hash.newInstance();
			final Array<Relation.Row> relations = Relation.getTable().selectRows(manager_connection);
			final Array<RelationRule.Row> rules = RelationRule.getTable().selectRows(manager_connection);
			for (final ManagedDatabase.Row database : databases)
			{
				final SqlConnection connection = this.getConnection(database.getResource());
				try
				{
					final String database_name = database.getName();
					final Array<InformationColumn.Row> information_columns = InformationColumn.getTable().selectRows(connection);
					database_columns.set(database_name, information_columns);
					//to match foo_bar_masters earlier than bar_masters. 
					final Array<InformationTable.Row> information_tables = InformationTable.getTable().selectRows(connection, "length(TABLE_NAME) DESC");
					database_tables.set(database_name, information_tables);
				}
				finally
				{
					connection.close();
				}
			}

			// もったいないのでキャッシュしておく.
			InformationTable.setCache(database_tables);
			InformationColumn.setCache(database_columns);

			// カラム情報追加
			for (final ManagedDatabase.Row database : databases)
			{
				final String database_name = database.getName();
				final Array<InformationColumn.Row> information_columns = database_columns.get(database_name);
				final Array<InformationTable.Row> information_tables = database_tables.get(database_name);
				for (final InformationColumn.Row information_column : information_columns)
				{
					final String table_name = information_column.getTableName();
					final String column_name = information_column.getColumnName();
					final int ordinal = information_column.getOrdinal();
					if (!ManagedColumn.getTable().hasRow(columns, database_name, table_name, column_name))
					{
						ManagedColumn.getTable().insertRow(manager_connection, database_name, table_name, column_name, ordinal, information_column.getComment());
					}
					else
					{
						final ManagedColumn.Row column = ManagedColumn.getTable().findRow(columns, database_name, table_name, column_name);
						if (column.getOrdinal() != ordinal)
						{
							ManagedColumn.getTable().updateRow(manager_connection, database_name, table_name, column_name, ordinal);
						}
					}
					if (database_name.equals("game")) {
						//relation ruleに基づいて、relationがあるか調べ、未追加のものは追加する.
						for (final InformationTable.Row information_table : information_tables) 
						{
							final String parent_table_name = information_table.getTableName();
							for (final RelationRule.Row rule : rules)
							{
								final String p = rule.formatPattern(parent_table_name);
								//logger.info("check %s %s %s %s", parent_table_name, p, column_name, table_name);
								if (column_name.matches(p))
								{
									//logger.info("column_name match: %s", column_name, parent_table_name, rule.getColumnName());
									if (!Relation.getTable().hasRow(relations, table_name, column_name) && 
										InformationColumn.getTable().hasRow(information_columns, parent_table_name, rule.getColumnName())) {
										//logger.info("add relation %s %s %s %s", parent_table_name, rule.getColumnName(), table_name, column_name);
										Relation.getTable().insertRow(manager_connection, database_name, parent_table_name, rule.getColumnName(), table_name, column_name);
										relations.add(new Relation.Row(database_name, parent_table_name, rule.getColumnName(), table_name, column_name));
									}
								}
							}
						}
					}
				}
			}
			// テーブル情報削除
			for (final ManagedColumn.Row column : columns)
			{
				final String database_name = column.getDatabase();
				final String table_name = column.getTable();
				final String column_name = column.getName();
				if (!database_columns.has(database_name))
				{
					ManagedColumn.getTable().deleteRow(manager_connection, database_name, table_name, column_name);
				}
				else
				{
					final Array<InformationColumn.Row> information_columns = database_columns.get(database_name);
					if (!InformationColumn.getTable().hasRow(information_columns, table_name, column_name))
					{
						ManagedColumn.getTable().deleteRow(manager_connection, database_name, table_name, column_name);
					}
				}
			}
		}
		catch (final RuntimeException cause)
		{
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
	 * リレーション情報を同期する
	 */
	private final void synchronizeRelation()
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<ManagedColumn.Row> columns = ManagedColumn.getTable().selectRows(connection);
			final Array<Relation.Row> relations = Relation.getTable().selectRows(connection);
			// リレーション削除
			for (final Relation.Row relation : relations)
			{
				final String database_name = relation.getDatabase();
				final String parent_table_name = relation.getParentTable();
				final String parent_column_name = relation.getParentColumn();
				if (!ManagedColumn.getTable().hasRow(columns, database_name, parent_table_name, parent_column_name))
				{
					Relation.getTable().deleteRowsFromParent(connection, database_name, parent_table_name, parent_column_name);
				}
				final String child_table_name = relation.getChildTable();
				final String child_column_name = relation.getChildColumn();
				if (!ManagedColumn.getTable().hasRow(columns, database_name, child_table_name, child_column_name))
				{
					Relation.getTable().deleteRowFromChild(connection, database_name, child_table_name, child_column_name);
				}
			}
		}
		catch (final RuntimeException cause)
		{
			connection.rollback();
			throw cause;
		}
		finally
		{
			connection.commit();
			connection.close();
		}
	}

	/**
	 * オペレータ別メニュー情報を同期する
	 */
	private final void synchronizeOperatorMenu()
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<Operator.Row> operators = Operator.getTable().selectRows(connection);
			final Array<Menu.Row> menus = Menu.getTable().selectRows(connection);
			final Array<OperatorMenu.Row> operator_menus = OperatorMenu.getTable().selectRows(connection);
			// オペレータ別メニュー情報追加
			for (final Operator.Row operator : operators)
			{
				final String operator_name = operator.getName();
				for (final Menu.Row menu : menus)
				{
					final String menu_name = menu.getName();
					if (!OperatorMenu.getTable().hasRow(operator_menus, operator_name, menu_name))
					{
						OperatorMenu.getTable().insertRow(connection, operator_name, menu_name);
					}
				}
			}
			// オペレータ別メニュー情報削除
			for (final OperatorMenu.Row operator_menu : operator_menus)
			{
				final String operator_name = operator_menu.getOperator();
				final String menu_name = operator_menu.getMenu();
				if (!Operator.getTable().hasRow(operators, operator_name))
				{
					OperatorMenu.getTable().deleteRow(connection, operator_name, menu_name);
				}
				else if (!Menu.getTable().hasRow(menus, menu_name))
				{
					OperatorMenu.getTable().deleteRow(connection, operator_name, menu_name);
				}
			}
		}
		catch (final RuntimeException cause)
		{
			connection.rollback();
			throw cause;
		}
		finally
		{
			connection.commit();
			connection.close();
		}
	}

	/**
	 * オペレータ別データベース情報を同期する
	 */
	private final void synchronizeOperatorDatabase()
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<Operator.Row> operators = Operator.getTable().selectRows(connection);
			final Array<ManagedDatabase.Row> databases = ManagedDatabase.getTable().selectRows(connection);
			final Array<OperatorDatabase.Row> operator_databases = OperatorDatabase.getTable().selectRows(connection);
			// オペレータ別データベース情報追加
			for (final Operator.Row operator : operators)
			{
				final String operator_name = operator.getName();
				for (final ManagedDatabase.Row database : databases)
				{
					final String database_name = database.getName();
					if (!OperatorDatabase.getTable().hasRow(operator_databases, operator_name, database_name))
					{
						OperatorDatabase.getTable().insertRow(connection, operator_name, database_name);
					}
				}
			}
			// オペレータ別データベース情報削除
			for (final OperatorDatabase.Row operator_database : operator_databases)
			{
				final String operator_name = operator_database.getOperator();
				final String database_name = operator_database.getDatabase();
				if (!Operator.getTable().hasRow(operators, operator_name))
				{
					OperatorDatabase.getTable().deleteRow(connection, operator_name, database_name);
				}
				else if (!ManagedDatabase.getTable().hasRow(databases, database_name))
				{
					OperatorDatabase.getTable().deleteRow(connection, operator_name, database_name);
				}
			}
		}
		catch (final RuntimeException cause)
		{
			connection.rollback();
			throw cause;
		}
		finally
		{
			connection.commit();
			connection.close();
		}
	}

	/**
	 * オペレータ別テーブル情報を同期する
	 */
	private final void synchronizeOperatorTable()
	{
		final Logger logger = this.getLogger(LoggerName.OPTION);
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<Operator.Row> operators = Operator.getTable().selectRows(connection);
			final Array<ManagedTable.Row> tables = ManagedTable.getTable().selectRows(connection);
			final Array<OperatorTable.Row> operator_tables = OperatorTable.getTable().selectRows(connection);
			// オペレータ別テーブル情報追加
			for (final Operator.Row operator : operators)
			{
				final String operator_name = operator.getName();
				for (final ManagedTable.Row table : tables)
				{
					final String database_name = table.getDatabase();
					final String table_name = table.getName();
					if (!OperatorTable.getTable().hasRow(operator_tables, operator_name, database_name, table_name))
					{
						OperatorTable.getTable().insertRow(connection, operator_name, database_name, table_name);
					}
				}
			}
			// オペレータ別テーブル情報削除
			for (final OperatorTable.Row operator_table : operator_tables)
			{
				final String operator_name = operator_table.getOperator();
				final String database_name = operator_table.getDatabase();
				final String table_name = operator_table.getTable();
				if (!Operator.getTable().hasRow(operators, operator_name))
				{
					OperatorTable.getTable().deleteRow(connection, operator_name, database_name, table_name);
				}
				else if (!ManagedTable.getTable().hasRow(tables, database_name, table_name))
				{
					OperatorTable.getTable().deleteRow(connection, operator_name, database_name, table_name);
				}
			}
		}
		catch (final RuntimeException cause)
		{
			connection.rollback();
			throw cause;
		}
		finally
		{
			connection.commit();
			connection.close();
		}
	}

	/**
	 * オペレータ別カラム情報を同期する
	 */
	private final void synchronizeOperatorColumn()
	{
		final SqlConnection connection = this.getConnection(DatabaseName.MANAGER);
		try
		{
			final Array<Operator.Row> operators = Operator.getTable().selectRows(connection);
			final Array<ManagedColumn.Row> columns = ManagedColumn.getTable().selectRows(connection);
			final Array<OperatorColumn.Row> operator_columns = OperatorColumn.getTable().selectRows(connection);
			// オペレータ別カラム情報追加
			for (final Operator.Row operator : operators)
			{
				final String operator_name = operator.getName();
				for (final ManagedColumn.Row column : columns)
				{
					final String database_name = column.getDatabase();
					final String table_name = column.getTable();
					final String column_name = column.getName();
					if (!OperatorColumn.getTable().hasRow(operator_columns, operator_name, database_name, table_name, column_name))
					{
						OperatorColumn.getTable().insertRow(connection, operator_name, database_name, table_name, column_name);
					}
				}
			}
			// オペレータ別カラム情報削除
			for (final OperatorColumn.Row operator_column : operator_columns)
			{
				final String operator_name = operator_column.getOperator();
				final String database_name = operator_column.getDatabase();
				final String table_name = operator_column.getTable();
				final String column_name = operator_column.getColumn();
				if (!Operator.getTable().hasRow(operators, operator_name))
				{
					OperatorColumn.getTable().deleteRow(connection, operator_name, database_name, table_name, column_name);
				}
				else if (!ManagedColumn.getTable().hasRow(columns, database_name, table_name, column_name))
				{
					OperatorColumn.getTable().deleteRow(connection, operator_name, database_name, table_name, column_name);
				}
			}
		}
		catch (final RuntimeException cause)
		{
			connection.rollback();
			throw cause;
		}
		finally
		{
			connection.commit();
			connection.close();
		}
	}
}
