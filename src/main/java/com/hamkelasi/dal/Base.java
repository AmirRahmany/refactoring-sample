package com.hamkelasi.dal;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Base {
    private static final HikariDataSource dataSource = createDataSource();

    private static HikariDataSource createDataSource() {
        String url = System.getProperty("db.url", System.getenv("DB_URL"));
        String user = System.getProperty("db.user", System.getenv("DB_USER"));
        String pass = System.getProperty("db.password", System.getenv("DB_PASSWORD"));

        Objects.requireNonNull(url, "Database URL must be provided via -Ddb.url or DB_URL");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        if (user != null) config.setUsername(user);
        if (pass != null) config.setPassword(pass);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setPoolName("HamkelasiDAL");
        return new HikariDataSource(config);
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean executeNonQuery(int sqlType, String commandText, SqlParameter... parameters) {
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(commandText)) {
            stmt.setEscapeProcessing(true);
            bindParameters(stmt, parameters);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public Object executeScalar(int sqlType, String commandText, SqlParameter... parameters) {
        try (Connection conn = getConnection();
             CallableStatement stmt = conn.prepareCall(commandText)) {
            bindParameters(stmt, parameters);
            boolean hasResult = stmt.execute();
            if (hasResult) {
                try (ResultSet rs = stmt.getResultSet()) {
                    if (rs.next()) {
                        return rs.getObject(1);
                    }
                }
            }
            // If scalar is returned via output parameter, try to read first OUT param
            for (SqlParameter p : parameters) {
                if (p.direction == SqlParameter.Direction.OUT || p.direction == SqlParameter.Direction.INOUT) {
                    return stmt.getObject(p.index);
                }
            }
            return null;
        } catch (SQLException ex) {
            return null;
        }
    }

    public List<Row> executeSelect(int sqlType, String commandText, SqlParameter... parameters) {
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
            return null;
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

        public Object get(int index) { return values.get(index); }
        public Object get(String column) {
            for (int i = 0; i < columnNames.size(); i++) {
                if (columnNames.get(i).equalsIgnoreCase(column)) return values.get(i);
            }
            return null;
        }
    }
}


