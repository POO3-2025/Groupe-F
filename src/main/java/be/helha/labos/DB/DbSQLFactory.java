package be.helha.labos.DB;

import java.sql.Connection;

/**
 *
 */
public interface DbSQLFactory {
    Connection createConnection();
}
