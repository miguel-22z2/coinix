package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
	private static final String URL = "jdbc:mysql://localhost:3306/coinix?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Xke@97!98#";

    public static Connection getConexao() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado.", e);
        }
    }
}
