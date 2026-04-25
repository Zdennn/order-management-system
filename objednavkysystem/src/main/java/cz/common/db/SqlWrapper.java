package cz.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlWrapper {

    private static Connection connection;

    public SqlWrapper() {
        initializeConnection();
    }

    private static synchronized void initializeConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/pg_systemobjednavka", "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet execQuery(String query, ArrayList<Param<?>> params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            setParams(statement, params);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet execUpdate(String query, ArrayList<Param<?>> params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            setParams(statement, params);
            statement.executeUpdate();
            return statement.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setParams(PreparedStatement statement, ArrayList<Param<?>> params) throws SQLException {
        for (Param<?> param : params) {
            switch (param.type()) {
                case "s":
                    statement.setString(param.order(), (String) param.value());
                    break;
                case "i":
                    statement.setInt(param.order(), (Integer) param.value());
                    break;
                case "b":
                    statement.setBoolean(param.order(), (Boolean) param.value());
                    break;
                case "d":
                    statement.setDouble(param.order(), (Double) param.value());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported parameter type: " + param.type());
            }
        }
    }
}