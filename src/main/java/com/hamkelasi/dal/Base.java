package com.hamkelasi.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Base {
    private static final Logger logger = LoggerFactory.getLogger(Base.class.getName());
    private static final DriverManagerDataSource dataSource = createDataSource();
    protected final JdbcTemplate jdbcTemplate;

    public Base() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static DriverManagerDataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://localhost:3306/hamkelasi?useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("springstudent");
        dataSource.setPassword("springstudent"); // Replace with your MariaDB password
        return dataSource;
    }

    protected static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Existing executeNonQuery for stored procedures (unchanged)
    public boolean executeNonQuery(int type, String commandText, SqlParameter... parameters) {
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(commandText)) {
            stmt.setEscapeProcessing(true);
            bindParameters(stmt, parameters);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // NEW METHOD: For plain SQL non-queries (INSERT/UPDATE/DELETE) using PreparedStatement
    public boolean executeNonQueryPrepared(String sql, SqlParameter... parameters) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            bindParametersToPreparedStatement(stmt, parameters);
            int rowsAffected = stmt.executeUpdate();
            logger.info("Executed non-query: {} (affected rows: {})", sql, rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException ex) {
            logger.error("Failed to execute non-query: " + sql, ex);
            return false;
        }
    }

    // Rest of the class remains unchanged...
    public Object executeScalar(int type, String sql, List<SqlParameter> params) {
        try {
            if (params == null || params.isEmpty()) {
                // For parameterless calls (e.g., CALL user_GetCount())
                return jdbcTemplate.queryForObject(sql, Integer.class);
            } else {
                // For parameterized calls (e.g., CALL province_GetProvinceCountByName(?))
                Object[] args = params.stream().map(SqlParameter::getValue).toArray();
                int[] argTypes = params.stream().mapToInt(SqlParameter::getSqlType).toArray();
                return jdbcTemplate.queryForObject(sql, args, argTypes, Integer.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute scalar query: " + sql, e);
        }
    }

    public List<Row> executeQuery(String sql, SqlParameter... parameters) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            bindParametersToPreparedStatement(stmt, parameters);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Row> rows = new ArrayList<>();
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                while (rs.next()) {
                    Row row = new Row(cols);
                    for (int i = 1; i <= cols; i++) {
                        row.add(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    rows.add(row);
                }
                return rows;
            }
        } catch (SQLException ex) {
            logger.error("Failed to execute query: " + sql, ex);
            throw new RuntimeException("Query execution failed", ex);
        }
    }

    private void bindParametersToPreparedStatement(PreparedStatement stmt, SqlParameter... parameters) throws SQLException {
        if (parameters == null) return;
        for (SqlParameter p : parameters) {
            if (p.direction != SqlParameter.Direction.IN) {
                throw new IllegalArgumentException("PreparedStatement only supports IN parameters");
            }
            stmt.setObject(p.index, p.value, p.sqlType);
        }
    }

    public List<Row> executeSelect(int type, String commandText, SqlParameter... parameters) {
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(commandText)) {
            bindParameters(stmt, parameters);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Row> rows = new ArrayList<>();
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                while (rs.next()) {
                    Row row = new Row(cols);
                    for (int i = 1; i <= cols; i++) {
                        row.add(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    rows.add(row);
                }
                return rows;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void bindParameters(CallableStatement stmt, SqlParameter... parameters) throws SQLException {
        if (parameters == null) return;
        for (SqlParameter p : parameters) {
            if (p.direction == SqlParameter.Direction.OUT) {
                stmt.registerOutParameter(p.index, p.sqlType);
            } else if (p.direction == SqlParameter.Direction.INOUT) {
                stmt.registerOutParameter(p.index, p.sqlType);
                stmt.setObject(p.index, p.value);
            } else {
                stmt.setObject(p.index, p.value);
            }
        }
    }

    public static class SqlParameter {
        public enum Direction { IN, OUT, INOUT }

        public final int index;
        public final int sqlType;
        public final Object value;
        public final Direction direction;

        private SqlParameter(int index, int sqlType, Object value, Direction direction) {
            this.index = index;
            this.sqlType = sqlType;
            this.value = value;
            this.direction = direction;
        }

        public static SqlParameter in(int index, int sqlType, Object value) {
            return new SqlParameter(index, sqlType, value, Direction.IN);
        }

        public static SqlParameter out(int index, int sqlType) {
            return new SqlParameter(index, sqlType, null, Direction.OUT);
        }

        public static SqlParameter inOut(int index, int sqlType, Object value) {
            return new SqlParameter(index, sqlType, value, Direction.INOUT);
        }

        public int getSqlType() {
            return sqlType;
        }

        public Object getValue() {
            return value;
        }
    }

    public static class Row {
        private final List<String> columnNames;
        private final List<Object> values;

        public Row(int capacity) {
            this.columnNames = new ArrayList<>(capacity);
            this.values = new ArrayList<>(capacity);
        }

        private void add(String column, Object value) {
            columnNames.add(column);
            values.add(value);
        }

        public Object get(int index) {
            return values.get(index);
        }

        public Object get(String column) {
            for (int i = 0; i < columnNames.size(); i++) {
                if (columnNames.get(i).equalsIgnoreCase(column)) return values.get(i);
            }
            return null;
        }
    }
}