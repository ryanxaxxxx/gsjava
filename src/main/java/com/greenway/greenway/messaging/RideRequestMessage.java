package com.greenway.greenway.messaging;

import lombok.Data;

@Data
public class RideRequestMessage {
    private String userId;
    private String destino;
}
