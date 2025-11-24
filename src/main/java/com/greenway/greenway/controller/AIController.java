package com.greenway.greenway.controller;

import com.greenway.greenway.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller para recursos de Inteligência Artificial Generativa
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@ConditionalOnBean(AIService.class)
public class AIController {

    private final AIService aiService;

    /**
     * Gera recomendações inteligentes de rotas sustentáveis
     * GET /api/ai/recomendacao?origem=São Paulo&destino=Campinas&transporte=bicicleta
     */
    @GetMapping("/recomendacao")
    public ResponseEntity<?> gerarRecomendacao(
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false, defaultValue = "qualquer") String transporte) {
        
        try {
            String recomendacao = aiService.gerarRecomendacaoRota(origem, destino, transporte);
            return ResponseEntity.ok(Map.of(
                    "recomendacao", recomendacao,
                    "origem", origem != null ? origem : "não informado",
                    "destino", destino != null ? destino : "não informado",
                    "transporte", transporte
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Não foi possível gerar a recomendação: " + e.getMessage()));
        }
    }

    /**
     * Gera dicas personalizadas de sustentabilidade
     * GET /api/ai/dicas?pontos=150&viagens=25
     */
    @GetMapping("/dicas")
    public ResponseEntity<?> gerarDicas(
            @RequestParam(defaultValue = "0") int pontos,
            @RequestParam(defaultValue = "0") int viagens) {
        
        try {
            String dicas = aiService.gerarDicasSustentabilidade(pontos, viagens);
            return ResponseEntity.ok(Map.of(
                    "dicas", dicas,
                    "pontosVerde", pontos,
                    "totalViagens", viagens
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Não foi possível gerar as dicas: " + e.getMessage()));
        }
    }

    /**
     * Analisa uma rota e fornece insights de sustentabilidade
     * POST /api/ai/analisar
     * Body: {"distancia": 15.5, "transporte": "bicicleta"}
     */
    @PostMapping("/analisar")
    public ResponseEntity<?> analisarRota(@RequestBody Map<String, Object> dados) {
        try {
            double distancia = dados.containsKey("distancia") ? 
                    Double.parseDouble(dados.get("distancia").toString()) : 0.0;
            String transporte = dados.containsKey("transporte") ? 
                    dados.get("transporte").toString() : "transporte padrão";
            
            String analise = aiService.analisarRota(distancia, transporte);
            return ResponseEntity.ok(Map.of(
                    "analise", analise,
                    "distanciaKm", distancia,
                    "modoTransporte", transporte
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Dados inválidos: " + e.getMessage()));
        }
    }
}

