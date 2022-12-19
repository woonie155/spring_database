package com.example.database.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.example.database.jdbc.JdbcConst.*;

@Slf4j
public class DBConnectionUtil {

    //DriverManager
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    //DriverManagerDataSource(DataSource 인터페이스)
    public static DriverManagerDataSource getDataSourceConnection(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        return driverManagerDataSource;
    }
}