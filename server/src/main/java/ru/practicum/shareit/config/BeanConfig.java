package ru.practicum.shareit.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
public class BeanConfig {
    Server inMemoryH2DatabaseServer;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile("test")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers",
                "-tcpPort", "9091");
    }
}
