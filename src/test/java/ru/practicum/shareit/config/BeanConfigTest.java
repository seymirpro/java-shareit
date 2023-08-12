package ru.practicum.shareit.config;

import org.h2.tools.Server;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.sql.SQLException;

import static org.mockito.Mockito.verify;

@SpringJUnitConfig
@SpringBootTest
public class BeanConfigTest {
    @Autowired
    private Server server;

    @Test
    public void testInMemoryH2DatabaseServerStartsTheServer() throws SQLException {
        server.start();

        verify(server).start();
    }

    @Configuration
    static class TestConfig {
        @Bean
        public Server server() {
            return Mockito.mock(Server.class);
        }

        @Bean
        public BeanConfig beanConfig(Server server) {
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.inMemoryH2DatabaseServer = server;
            return beanConfig;
        }
    }
}