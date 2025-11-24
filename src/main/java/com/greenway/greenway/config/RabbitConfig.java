package com.greenway.greenway.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(RabbitTemplate.class)
public class RabbitConfig {

    @Bean
    public Queue rideRequestsQueue() {
        return new Queue("ride-requests", true);
    }
}
