package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private final String host = "localhost";
    private final String port = "1433";
    private final String dbname = "Qly_nhansu";
    private final String userName = "sa";
    private final String password = "12345";

    private final String url =
        "jdbc:sqlserver://" + host + ":" + port +
        ";databaseName=" + dbname +
        ";encrypt=true;trustServerCertificate=true";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }
}
