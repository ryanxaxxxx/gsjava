package com.greenway.greenway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MapService {

    @Value("${ors.api.key}")
    private String apiKey;

    @Value("${ors.api.url}")
    private String apiUrl;

    private final WebClient.Builder webClientBuilder;

    @Cacheable(value = "routes", key = "#origemLng + '_' + #origemLat + '_' + #destinoLng + '_' + #destinoLat")
    public String calcularRota(double origemLng, double origemLat, double destinoLng, double destinoLat) {

        Map<String, Object> body = Map.of(
            "coordinates", List.of(
                List.of(origemLng, origemLat),
                List.of(destinoLng, destinoLat)
            )
        );

        return webClientBuilder.build()
            .post()
            .uri(apiUrl)
            .header("Authorization", apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
