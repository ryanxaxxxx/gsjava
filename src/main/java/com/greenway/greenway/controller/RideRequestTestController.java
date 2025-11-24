package com.greenway.greenway.controller;

import com.greenway.greenway.messaging.RideRequestMessage;
import com.greenway.greenway.messaging.RideRequestProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/rabbit")
@RequiredArgsConstructor
@ConditionalOnBean(RideRequestProducer.class)
public class RideRequestTestController {

    private final RideRequestProducer producer;

    @PostMapping("/send")
    public String sendTestMessage() {
        RideRequestMessage msg = new RideRequestMessage();
        msg.setUserId("123");
        msg.setDestino("Av. Paulista");

        producer.send(msg);

        return "Mensagem enviada!";
    }
}
