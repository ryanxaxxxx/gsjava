package com.greenway.greenway.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnBean(RabbitTemplate.class)
public class RideRequestListener {

    @RabbitListener(queues = "ride-requests")
    public void handle(RideRequestMessage msg) {

        System.out.println("📩 Mensagem recebida do RabbitMQ:");
        System.out.println("→ Usuário: " + msg.getUserId());
        System.out.println("→ Destino: " + msg.getDestino());

        
    }
}
