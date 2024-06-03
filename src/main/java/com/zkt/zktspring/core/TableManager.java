package com.zkt.zktspring.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableManager {
    private static final String URL = "jdbc:mysql://localhost:3306/zkteco";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private String table;

    /**
     * This constructor sets the table name and connects to the database.
     * 
     * @param table
     */
    public TableManager(String table) {
        this.table = table;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Connected successfully!");
            this.connection = connection;
        } catch (SQLException e) {
            System.out.println("Unable to connect to the database.");
            e.printStackTrace();
        }
    }

    /**
     * Get all data from the table.
     * 
     * @return List of map of string and object
     * @outputExample output = [{ "id": 1, "name": "John Doe",...}, { "id": 2,
     *                "name":"Jane Doe",...}]
     */
    public List<Map<String, Object>> getAll() {
        return executeFetchQuery("SELECT * FROM " + this.table);
    }

    /**
     * Get data by id.
     * 
     * @param id
     * @return List of map of string and object
     * @outputExample output = [{ "id": 1, "name": "John Doe",...}]
     */
    public Map<String, Object> getById(Long id) {
        List<Map<String, Object>> user = executeFetchQuery("SELECT * FROM " + this.table + " WHERE id = " + id);
        if (user.isEmpty()) {
            return null;
        }
        return user.get(0);
    }

    /**
     * Get data by filter.
     * 
     * @param filters
     * @return List of map of string and object
     * @filterExample filters = { "id": 1, "order_by": "id DESC", "limit": 2,
     *                "offset": 1}
     * @outputExample output = [{ "id": 1, "name": "John Doe",...}, { "id": 2,
     *                "name":
     *                "Jane Doe",...}]
     */
    public List<Map<String, Object>> getByFilter(Map<String, Object> filters) {
        return executeFetchQuery("SELECT * FROM " + this.table + buildWhereClause(filters));
    }

    /**
     * Insert a data to the table.
     * 
     * @param data
     * @throws SQLException
     * @dataExample data = { "name": "John Doe", "email": "example@example.com",
     *              ...}
     * @return int- number of rows affected
     */
    public int insert(Map<String, Object> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        if (data.isEmpty()) {
            return 0;
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            columns.append(entry.getKey() + ",");
            values.append("'" + entry.getValue() + "',");
        }

        String columnsString = columns.substring(0, columns.length() - 1);
        String valuesString = values.substring(0, values.length() - 1);

        String sql = "INSERT INTO " + this.table + " (" + columnsString + ") VALUES (" + valuesString + ")";
        System.out.println(sql);
        try {
            if (this.connection.isClosed()) {
                System.out.println("Reconnecting...");
                this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            try (Statement statement = this.connection.createStatement()) {
                System.out.println("Inserting data...");
                return (statement.executeUpdate(sql));
            }
        } catch (SQLException e) {
            System.out.println("Unable to insert data.");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Insert multiple data to the table.
     *
     * @param data
     * @throws SQLException
     * @dataExample data = [{ "name": "John Doe", "email":
     *              "example@example.com",...}, { "name": "Jane Doe", "email":
     *              "example@example.com",...}]
     * @return int- number of rows affected
     */
    public int insertMultiple(List<Map<String, Object>> data) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        // if data is empty return 0
        if (data.isEmpty()) {
            return 0;
        }

        for (Map.Entry<String, Object> entry : data.get(0).entrySet()) {
            columns.append(entry.getKey() + ",");
        }

        String columnsString = columns.substring(0, columns.length() - 1);

        for (Map<String, Object> row : data) {
            StringBuilder value = new StringBuilder();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                value.append("'" + entry.getValue() + "',");
            }
            values.append("(" + value.substring(0, value.length() - 1) + "),");
        }

        String valuesString = values.substring(0, values.length() - 1);

        String sql = "INSERT INTO " + this.table + " (" + columnsString + ") VALUES " + valuesString;
        System.out.println(sql);
        try {
            if (this.connection.isClosed()) {
                System.out.println("Reconnecting...");
                this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            try (Statement statement = this.connection.createStatement()) {
                System.out.println("Inserting data...");
                return (statement.executeUpdate(sql));
            }
        } catch (SQLException e) {
            System.out.println("Unable to insert data.");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Update data in the table.
     * 
     * @return int- number of rows affected
     * @param data
     * @param id
     * @throws SQLException
     * @example data = { "name": "John Doe", "email": "example.example.com", ...}
     */
    public int update(Map<String, Object> data, Long id) {
        System.out.println(data);
        StringBuilder set = new StringBuilder();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            set.append(entry.getKey() + " = '" + entry.getValue() + "',");
        }

        String setString = set.substring(0, set.length() - 1);

        String sql = "UPDATE " + this.table + " SET " + setString + " WHERE id = " + id;
        System.out.println(sql);
        try {
            if (this.connection.isClosed()) {
                System.out.println("Reconnecting...");
                this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            try (Statement statement = this.connection.createStatement()) {
                System.out.println("Updating data...");
                return (statement.executeUpdate(sql));
            }
        } catch (SQLException e) {
            System.out.println("Unable to update data.");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Delete data from the table.
     * 
     * @return int- number of rows affected
     * @param id
     * @throws SQLException
     */
    public int delete(Long id) {
        String sql = "DELETE FROM " + this.table + " WHERE id = " + id;
        System.out.println(sql);
        try {
            if (this.connection.isClosed()) {
                System.out.println("Reconnecting...");
                this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            try (Statement statement = this.connection.createStatement()) {
                System.out.println("Data deleted successfully.");
                return (statement.executeUpdate(sql));
            }
        } catch (SQLException e) {
            System.out.println("Unable to delete data.");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Build where clause from the filters.
     * 
     * @param filters
     * @return String
     * 
     * @example filters = { "id": 1, "order_by": "id DESC", "limit": 2, "offset": 1}
     */
    public static String buildWhereClause(Map<String, Object> filters) {
        StringBuilder whereClause = new StringBuilder();
        String whereClauseString = "";
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            // handle order by
            if (entry.getKey().equals("order_by")) {
                continue;
            }
            // handle limit
            if (entry.getKey().equals("limit")) {
                continue;
            }
            // handle offset
            if (entry.getKey().equals("offset")) {
                continue;
            }
            whereClause.append("WHERE ");
            whereClause.append(entry.getKey() + " = '" + entry.getValue() + "' AND ");
            whereClauseString = whereClause.substring(0, whereClause.length() - 5);
        }

        if (filters.containsKey("order_by")) {
            whereClauseString += " ORDER BY " + filters.get("order_by");
        }

        if (filters.containsKey("limit")) {
            whereClauseString += " LIMIT " + filters.get("limit");
        }

        if (filters.containsKey("offset")) {
            whereClauseString += " OFFSET " + filters.get("offset");
        }

        System.out.println(whereClauseString);
        return whereClauseString;

    }

    /**
     * Execute fetch query and return the result as a list of maps.
     * 
     * @param sql
     * @return List of map of string and object
     * @throws SQLException
     */
    public List<Map<String, Object>> executeFetchQuery(String sql) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            if (this.connection.isClosed()) {
                System.out.println("Reconnecting...");
                this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }

            try (Statement statement = this.connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), resultSet.getObject(i));
                    }
                    resultList.add(row);
                }

                return resultList;
            }
        } catch (SQLException e) {
            System.out.println("Unable to execute query.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Close the connection.
     * 
     * @param connection
     */
    public static void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
