package com.veterinaria.mascota.config;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;

public final class DataSourceProvider {

    private DataSourceProvider() {
    }

    public static PoolDataSource get() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final PoolDataSource INSTANCE = createInstance();

        private static PoolDataSource createInstance() {
            try {
                String tns = System.getenv("TNS_ADMIN");
                if (tns == null || tns.isBlank()) {
                    throw new IllegalStateException(
                            "TNS_ADMIN no está definido. Configure la ruta del wallet en App Settings.");
                }
                System.setProperty("oracle.net.tns_admin", tns);

                String user = mustGet("DB_USER");
                String pass = mustGet("DB_PASSWORD");
                String alias = mustGet("DB_CONNECT_STRING"); // ej: myadb_tp

                System.out.println("TNS_ADMIN: " + tns);
                System.out.println("DB_USER: " + user);
                System.out.println("DB_PASSWORD: " + pass);
                System.out.println("DB_CONNECT_STRING: " + alias);
                
                PoolDataSource ds = PoolDataSourceFactory.getPoolDataSource();
                ds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
                ds.setURL("jdbc:oracle:thin:@" + alias);
                ds.setUser(user);
                ds.setPassword(pass);

                ds.setInitialPoolSize(0);
                ds.setMinPoolSize(0);
                ds.setMaxPoolSize(4);
                ds.setInactiveConnectionTimeout(60);
                ds.setAbandonedConnectionTimeout(60);
                ds.setTimeoutCheckInterval(30);
                ds.setValidateConnectionOnBorrow(true);
                return ds;
            } catch (Exception e) {
                throw new RuntimeException("Error creando DataSource: " + e.getMessage(), e);
            }
        }

        private static String mustGet(String k) {
            String v = System.getenv(k);
            if (v == null || v.isBlank())
                throw new IllegalStateException("Falta variable de entorno: " + k);
            return v;
        }

    }
}
