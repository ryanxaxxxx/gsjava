package com.greenway.greenway.dto;

public class RideRequestMessage {
    private String userId;
    private String destino;

    public RideRequestMessage() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
