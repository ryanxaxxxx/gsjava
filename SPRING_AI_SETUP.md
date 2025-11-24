# 🤖 Configuração do Spring AI

## Status da Implementação

✅ **Serviço de IA Implementado**: `AIService` e `AIController` criados
✅ **Funcionalidades**:
   - Recomendações inteligentes de rotas sustentáveis
   - Dicas personalizadas de sustentabilidade
   - Análise de impacto ambiental de rotas

⚠️ **Dependência Spring AI**: Pode precisar de ajuste na versão

## Opções de Configuração

### Opção 1: Usar Spring AI (Recomendado)

Se a dependência `spring-ai-openai-spring-boot-starter` estiver disponível:

1. Configure a chave da API OpenAI no `application.properties` ou variável de ambiente:
   ```properties
   spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY}
   ```

2. Ou configure no Azure App Service:
   - Variável: `SPRING_AI_OPENAI_API_KEY`
   - Valor: sua chave da API OpenAI

### Opção 2: Usar Azure OpenAI (Alternativa)

Se preferir usar Azure OpenAI:

1. Adicione a dependência no `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.ai</groupId>
       <artifactId>spring-ai-azure-openai-spring-boot-starter</artifactId>
       <version>1.0.0-M3</version>
   </dependency>
   ```

2. Configure no `application.properties`:
   ```properties
   spring.ai.azure.openai.api-key=${AZURE_OPENAI_API_KEY}
   spring.ai.azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT}
   spring.ai.azure.openai.deployment-name=gpt-35-turbo
   ```

### Opção 3: Implementação Customizada (Sem Spring AI)

Se a dependência do Spring AI não estiver disponível, você pode:

1. Remover a dependência do `pom.xml`
2. Criar uma implementação customizada usando `WebClient` para chamar diretamente a API OpenAI
3. O serviço `AIService` está configurado como `@ConditionalOnBean(ChatClient.class)`, então não causará erros se o Spring AI não estiver disponível

## Endpoints Disponíveis

### 1. Recomendação de Rota
```
GET /api/ai/recomendacao?origem=São Paulo&destino=Campinas&transporte=bicicleta
```

### 2. Dicas de Sustentabilidade
```
GET /api/ai/dicas?pontos=150&viagens=25
```

### 3. Análise de Rota
```
POST /api/ai/analisar
Content-Type: application/json

{
  "distancia": 15.5,
  "transporte": "bicicleta"
}
```

## Testando Localmente

1. Configure a variável de ambiente:
   ```bash
   export SPRING_AI_OPENAI_API_KEY=sua-chave-aqui
   ```

2. Inicie a aplicação:
   ```bash
   mvn spring-boot:run
   ```

3. Teste os endpoints:
   ```bash
   curl "http://localhost:8080/api/ai/recomendacao?origem=São Paulo&destino=Campinas&transporte=bicicleta"
   ```

## Notas Importantes

- O serviço está configurado como **condicional** (`@ConditionalOnBean`), então a aplicação funcionará mesmo sem Spring AI configurado
- Se a dependência não estiver disponível no Maven Central, você pode:
  - Usar uma versão diferente
  - Implementar uma integração customizada com OpenAI
  - Usar Azure OpenAI como alternativa
- Para produção no Azure, configure a chave da API como variável de ambiente no App Service

## Troubleshooting

### Erro: "Dependency not found"
- Verifique se a versão do Spring AI está correta
- Tente usar uma versão mais recente ou mais antiga
- Considere usar Azure OpenAI como alternativa

### Erro: "ChatClient bean not found"
- Isso é esperado se o Spring AI não estiver configurado
- O serviço não será carregado, mas a aplicação funcionará normalmente
- Configure a chave da API para habilitar o serviço

### Erro: "API key not configured"
- Configure a variável `SPRING_AI_OPENAI_API_KEY` no ambiente
- Ou configure no `application.properties` (não recomendado para produção)

