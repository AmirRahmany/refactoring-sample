package com.hamkelasi;

import com.hamkelasi.dal.User;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest {

    @Test
    public void testUserGetCount() {
        User dalUser = new User();
        int count = dalUser.getCount();
        assertTrue(count >= 0);
    }
}
