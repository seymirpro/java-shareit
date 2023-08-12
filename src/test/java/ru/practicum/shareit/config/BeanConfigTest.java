package ru.practicum.shareit.config;

import org.h2.tools.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest
public class BeanConfigTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Test
    public void testInMemoryH2DatabaseServerBeanCreation() {
        ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                .withUserConfiguration(BeanConfig.class);

        contextRunner.run(context -> {
            Server server = applicationContext.getBean(Server.class);

            Assertions.assertNotNull(server);
            Assertions.assertTrue(server.isRunning(true));
        });
    }
}