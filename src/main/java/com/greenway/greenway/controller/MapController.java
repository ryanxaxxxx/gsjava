package com.greenway.greenway.controller;

import com.greenway.greenway.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/rota")
    public String rota(
            @RequestParam double origemLng,
            @RequestParam double origemLat,
            @RequestParam double destinoLng,
            @RequestParam double destinoLat
    ) {
        return mapService.calcularRota(origemLng, origemLat, destinoLng, destinoLat);
    }
}
