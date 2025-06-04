//package com.github.maximslepukhin.onlinestore;
//
//import net.bytebuddy.utility.dispatcher.JavaDispatcher;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//
//public final class PostgresTestcontainer {
//
//    @JavaDispatcher.Container
//    @ServiceConnection
//    static final PostgreSQLContainer<?> postgresContainer =
//            new PostgreSQLContainer<>("postgres:15.3-alpine")
//                    .withDatabaseName("testdb")
//                    .withUsername("testuser")
//                    .withPassword("testpass");
//}