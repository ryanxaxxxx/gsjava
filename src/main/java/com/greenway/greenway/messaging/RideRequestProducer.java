package com.greenway.greenway.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(RabbitTemplate.class)
public class RideRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(RideRequestMessage msg) {
        rabbitTemplate.convertAndSend("ride-requests", msg);
    }

}
