package com.greenway.greenway;

import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class RabbitMQTestConfiguration {
    
    // Mock do RabbitTemplate para evitar dependência real do RabbitMQ nos testes
    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate() {
        return Mockito.mock(RabbitTemplate.class);
    }
}

