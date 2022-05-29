package com.test.connection;

import com.test.model.EventLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class HsqldbConnection {

    private static final Logger logger
            = LoggerFactory.getLogger(HsqldbConnection.class);

    private static final String createTableSQL = "create table eventLog (\r\n" + "  id  varchar(50) primary key,\r\n" +
            "  state varchar(50),\r\n" + "  type varchar(50),\r\n" + "  host varchar(50),\r\n" +
            "  logTime timestamp \r\n" + "  );";

    private static final String INSERT_USERS_SQL = "INSERT INTO eventLog" +
            "  (id, state, type, host, logTime) VALUES " +
            " (?, ?, ?, ?, ?);";

    public static Connection getFileConnect() throws SQLException {
        logger.info("start Establish  connection");
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:D:/opt/db/testdb",
                "sa",
                "");
        logger.info("connection object created");
        return conn;
    }

    public static void createTable(){
        logger.info(createTableSQL);
        // Step 1: Establishing a Connection
        try {
            Connection connection = HsqldbConnection.getFileConnect();
            // Step 2:Create a statement using connection object
            Statement statement = connection.createStatement();
            // Step 3: Execute the query or update query
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            // print SQL exception information
            e.printStackTrace();
        }
    }

    public static void insertRecords(EventLog eventLog) {
        logger.debug("insert into eventLog table");
        // Step 1: Establishing a Connection
        try {
            Connection connection = HsqldbConnection.getFileConnect();
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
            preparedStatement.setString(1,eventLog.getId() );
            preparedStatement.setString(2, String.valueOf(eventLog.getState()));
            preparedStatement.setString(3, eventLog.getType());
            preparedStatement.setString(4, eventLog.getHost());
            preparedStatement.setTimestamp(5, eventLog.getTimestamp());
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            e.printStackTrace();
        }
    }

}
