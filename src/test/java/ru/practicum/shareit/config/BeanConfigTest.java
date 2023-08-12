package ru.practicum.shareit.config;

import org.h2.tools.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = BeanConfig.class)
public class BeanConfigTest {

    @Mock
    private Server inMemoryH2DatabaseServer;

    @Autowired
    private BeanConfig beanConfig;

    @BeforeEach
    public void setup() throws Exception {
        when(inMemoryH2DatabaseServer.isRunning(anyBoolean())).thenReturn(true);
    }

    @Test
    public void testInMemoryH2DatabaseServerBeanCreation() throws Exception {
        Assertions.assertNotNull(inMemoryH2DatabaseServer);
        Assertions.assertTrue(inMemoryH2DatabaseServer.isRunning(true));
    }
}