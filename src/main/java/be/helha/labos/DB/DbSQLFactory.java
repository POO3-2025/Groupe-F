package be.helha.labos.DB;

import java.sql.Connection;

/**
 * Interface pour la création de connexions à une base de données SQL.
 */
public interface DbSQLFactory {
    Connection createConnection();
}