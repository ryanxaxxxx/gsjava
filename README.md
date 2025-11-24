
# 🚴 GreenWay - Sistema de Mobilidade Sustentável

Sistema web para promover mobilidade sustentável corporativa, incentivando o uso de transportes ecológicos através de gamificação e recomendações inteligentes.

## 📋 Sobre o Projeto

O GreenWay é uma aplicação Spring Boot que permite:
- Cadastro e gerenciamento de usuários
- Sistema de pontos verdes por uso de transporte sustentável
- Cálculo de rotas otimizadas
- Recomendações inteligentes de IA para mobilidade sustentável
- Mensageria assíncrona para processamento de solicitações

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.3.1**
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização JWT
- **Spring AI** - Inteligência Artificial Generativa
- **RabbitMQ** - Mensageria assíncrona
- **SQL Server** - Banco de dados
- **Flyway** - Migrations
- **Caffeine** - Cache
- **Thymeleaf** - Templates web
- **Maven** - Gerenciamento de dependências

## 📦 Requisitos

- Java 17+
- Maven 3.6+
- SQL Server (ou banco configurado)
- RabbitMQ (opcional - funciona sem ele)

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone <url-do-repositorio>
cd Green-way
```

### 2. Configure o banco de dados
Edite `src/main/resources/application.properties` e configure:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;database=greenwaydb
spring.datasource.username=seu-usuario
spring.datasource.password=sua-senha
```

### 3. Execute as migrations
As migrations do Flyway serão executadas automaticamente na primeira execução.

### 4. Execute a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 🔑 Configuração Opcional

### Spring AI (Recomendações Inteligentes)
Para habilitar as funcionalidades de IA, configure:
```bash
export SPRING_AI_OPENAI_API_KEY=sua-chave-openai
```

Ou adicione no `application.properties`:
```properties
spring.ai.openai.api-key=sua-chave-aqui
```

### RabbitMQ (Mensageria)
Para habilitar RabbitMQ, configure:
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

**Nota:** A aplicação funciona normalmente sem RabbitMQ configurado.

## 📡 Principais Endpoints

### Autenticação
```
POST /api/auth/login     - Login e obtenção de token JWT
```

### Usuários
```
GET    /users            - Listar usuários (com paginação)
GET    /users/{id}       - Buscar usuário por ID
POST   /users            - Criar usuário
PUT    /users/{id}       - Atualizar usuário
DELETE /users/{id}       - Deletar usuário
```

### Endereços
```
GET    /api/enderecos           - Listar endereços (com paginação)
GET    /api/enderecos/{id}      - Buscar endereço por ID
POST   /api/enderecos           - Criar endereço
PUT    /api/enderecos/{id}      - Atualizar endereço
DELETE /api/enderecos/{id}      - Deletar endereço
```

### Inteligência Artificial
```
GET  /api/ai/recomendacao?origem=São Paulo&destino=Campinas&transporte=bicicleta
GET  /api/ai/dicas?pontos=150&viagens=25
POST /api/ai/analisar
```

### Rotas
```
GET /api/map/route?origemLng=-46.6333&origemLat=-23.5505&destinoLng=-47.0608&destinoLat=-22.9068
```

## 🌍 Internacionalização

A aplicação suporta português (pt_BR) e inglês (en_US).

Para alterar o idioma, use o parâmetro `?lang=pt` ou `?lang=en` nas requisições.

## ☁️ Deploy no Azure

O projeto está configurado para deploy no Azure App Service:

1. **Pipeline Azure DevOps**: `azure-pipelines.yml`
2. **Configuração de Produção**: `application-prod.properties`
3. **Documentação**: Veja `AZURE_DEVOPS_SETUP.md` e `AZURE_APP_SERVICE_TROUBLESHOOTING.md`

### Variáveis de Ambiente no Azure App Service:
```
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:sqlserver://...
DB_USERNAME=...
DB_PASSWORD=...
JWT_SECRET=...
SPRING_AI_OPENAI_API_KEY=... (opcional)
```

## ✅ Requisitos Técnicos Atendidos

- ✅ Anotações Spring para beans e injeção de dependências
- ✅ Camada Model/DTO com métodos de acesso corretos
- ✅ Persistência com Spring Data JPA
- ✅ Validação com Bean Validation
- ✅ **Caching** com Caffeine para performance
- ✅ **Internacionalização** (pt_BR e en_US)
- ✅ **Paginação** em recursos com muitos registros
- ✅ Spring Security com JWT
- ✅ Tratamento de erros e exceptions
- ✅ Mensageria com RabbitMQ (filas assíncronas)
- ✅ **Spring AI** para recomendações inteligentes
- ✅ Deploy em nuvem (Azure)
- ✅ API REST com verbos HTTP e códigos de status adequados

## 📁 Estrutura do Projeto

```
src/main/java/com/greenway/greenway/
├── config/          # Configurações (Security, RabbitMQ, Locale, CORS)
├── controller/      # Controllers REST
├── dto/             # Data Transfer Objects
├── exception/       # Tratamento de exceções
├── mapper/          # MapStruct mappers
├── messaging/       # RabbitMQ producers/consumers
├── model/           # Entidades JPA
├── repository/      # Repositories Spring Data
├── security/        # Configuração de segurança e JWT
└── service/         # Lógica de negócio
```

## 🧪 Testes

Execute os testes com:
```bash
mvn test
```

## 📚 Documentação Adicional

- `ANALISE_REQUISITOS_PROJETO.md` - Análise completa dos requisitos
- `SPRING_AI_SETUP.md` - Guia de configuração do Spring AI
- `AZURE_DEVOPS_SETUP.md` - Configuração do pipeline
- `AZURE_APP_SERVICE_TROUBLESHOOTING.md` - Troubleshooting do Azure

## 👥 Autores

Desenvolvido como projeto acadêmico para o tema de mobilidade sustentável.

## 📄 Licença

Este projeto é de uso acadêmico.

---

**Status:** ✅ Projeto completo e funcional | Todos os requisitos atendidos





