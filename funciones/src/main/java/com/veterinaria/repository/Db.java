package com.veterinaria.repository;

import com.veterinaria.config.AppConfig;
import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Db {

    private static volatile DataSource ds;

    private static DataSource build() throws SQLException {
        System.setProperty("oracle.net.tns_admin", AppConfig.tnsAdmin());
        OracleDataSource ods = new OracleDataSource();
        ods.setURL("jdbc:oracle:thin:@" + AppConfig.alias());
        ods.setUser(AppConfig.user());
        ods.setPassword(AppConfig.password());
        return ods;
    }

    public static DataSource dataSource() throws SQLException {
        if (ds == null) {
            synchronized (Db.class) {
                if (ds == null)
                    ds = build();
            }
        }
        return ds;
    }

    public static Connection open() throws SQLException {
        return dataSource().getConnection();
    }
}
