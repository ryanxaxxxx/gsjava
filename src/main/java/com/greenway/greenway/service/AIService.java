package com.greenway.greenway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Serviço de Inteligência Artificial Generativa
 * Fornece recomendações inteligentes para mobilidade sustentável
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(ChatClient.class)
public class AIService {

    private final ChatClient chatClient;

    @Value("${spring.ai.openai.api-key:}")
    private String apiKey;

    /**
     * Gera recomendações inteligentes de rotas sustentáveis
     * baseado em origem, destino e preferências do usuário
     */
    public String gerarRecomendacaoRota(String origem, String destino, String preferenciaTransporte) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate("""
                Você é um assistente especializado em mobilidade sustentável.
                Um usuário quer ir de {origem} para {destino} usando {preferenciaTransporte}.
                
                Forneça uma recomendação inteligente e sustentável considerando:
                1. A melhor opção de transporte considerando impacto ambiental
                2. Sugestões de rotas alternativas mais ecológicas
                3. Dicas para reduzir a pegada de carbono
                4. Estimativa de economia de CO2 comparada a transporte tradicional
                
                Seja conciso e prático. Responda em português brasileiro.
                """);

            Prompt prompt = promptTemplate.create(Map.of(
                    "origem", origem != null ? origem : "ponto de partida",
                    "destino", destino != null ? destino : "destino",
                    "preferenciaTransporte", preferenciaTransporte != null ? preferenciaTransporte : "qualquer meio"
            ));

            String response = chatClient.call(prompt.getContents());
            log.info("Recomendação de rota gerada com sucesso");
            return response;

        } catch (Exception e) {
            log.error("Erro ao gerar recomendação de IA", e);
            return "Desculpe, não foi possível gerar uma recomendação no momento. " +
                   "Por favor, tente novamente mais tarde.";
        }
    }

    /**
     * Gera dicas personalizadas de sustentabilidade para o usuário
     * baseado no seu histórico de viagens
     */
    public String gerarDicasSustentabilidade(int pontosVerde, int totalViagens) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate("""
                Você é um assistente especializado em mobilidade sustentável.
                Um usuário tem {pontosVerde} pontos verdes acumulados e fez {totalViagens} viagens.
                
                Forneça dicas personalizadas e motivacionais para:
                1. Aumentar o uso de transporte sustentável
                2. Reduzir a pegada de carbono
                3. Ganhar mais pontos verdes
                4. Contribuir para um planeta mais sustentável
                
                Seja motivador e prático. Responda em português brasileiro.
                """);

            Prompt prompt = promptTemplate.create(Map.of(
                    "pontosVerde", String.valueOf(pontosVerde),
                    "totalViagens", String.valueOf(totalViagens)
            ));

            String response = chatClient.call(prompt.getContents());
            log.info("Dicas de sustentabilidade geradas com sucesso");
            return response;

        } catch (Exception e) {
            log.error("Erro ao gerar dicas de IA", e);
            return "Continue usando transporte sustentável para ganhar mais pontos verdes!";
        }
    }

    /**
     * Analisa e sugere otimizações para uma rota específica
     */
    public String analisarRota(double distanciaKm, String modoTransporte) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate("""
                Você é um assistente especializado em mobilidade sustentável.
                Analise uma rota de {distanciaKm} km usando {modoTransporte}.
                
                Forneça:
                1. Impacto ambiental estimado (em kg de CO2)
                2. Comparação com outros modos de transporte
                3. Sugestões para tornar a viagem mais sustentável
                4. Benefícios ambientais da escolha atual
                
                Seja técnico mas acessível. Responda em português brasileiro.
                """);

            Prompt prompt = promptTemplate.create(Map.of(
                    "distanciaKm", String.valueOf(distanciaKm),
                    "modoTransporte", modoTransporte != null ? modoTransporte : "transporte padrão"
            ));

            String response = chatClient.call(prompt.getContents());
            log.info("Análise de rota gerada com sucesso");
            return response;

        } catch (Exception e) {
            log.error("Erro ao analisar rota com IA", e);
            return "Análise não disponível no momento.";
        }
    }
}

