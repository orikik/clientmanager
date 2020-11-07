package com.orikik.clientmanager;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class RepositoryTestBase {
    public static final PostgreSQLContainer postgreSQLContainer;
    private static final String USER_NAME = "user";
    private static final String USER_PASSWORD = "pass";
    private static final String DB_NAME = "clientmanagerdb";
    private static final String IMAGE = "postgres:latest";

    static {
        postgreSQLContainer = new PostgreSQLContainer(IMAGE)
                .withUsername(USER_NAME)
                .withPassword(USER_PASSWORD)
                .withDatabaseName(DB_NAME);
        postgreSQLContainer.start();
        System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl());
        System.setProperty("DB_USER", postgreSQLContainer.getUsername());
        System.setProperty("DB_PASS", postgreSQLContainer.getPassword());
    }

    @Before
    public void before() {
        Flyway flyway = Flyway.configure().dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword()).load();
        flyway.clean();
        flyway.migrate();
    }

}
