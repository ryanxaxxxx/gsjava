# Configuração do Flyway no GreenWay

## ✅ O que foi configurado

### 1. Dependências
- ✅ Adicionada dependência `flyway-core` no `pom.xml`
- ✅ Versão gerenciada automaticamente pelo Spring Boot

### 2. Migrations criadas
- ✅ `V1__create_usuario.sql` - Drop das tabelas existentes (baseline limpo)
- ✅ `V2__create_users.sql` - Cria a tabela `users`
- ✅ `V3__create_endereco.sql` - Cria a tabela `endereco`

### 3. Configurações por perfil

#### Desenvolvimento (`application-dev.properties`)
- Flyway **desabilitado** (usa H2 com `ddl-auto=update`)
- Banco em memória H2

#### Testes (`application-test.properties`)
- Flyway **desabilitado** (usa H2 com `ddl-auto=create-drop`)
- Banco em memória H2

#### Produção (`application-prod.properties`)
- Flyway **habilitado**
- SQL Server com migrations automáticas
- Configuração pronta para Azure DevOps

## 📁 Estrutura de arquivos

```
src/main/resources/
├── db/
│   └── migration/
│       ├── V1__create_usuario.sql
│       ├── V2__create_users.sql
│       └── V3__create_endereco.sql
├── application.properties
├── application-dev.properties
├── application-prod.properties
└── application-test.properties
```

## 🚀 Como usar em produção (Azure DevOps)

### 1. Configurar variáveis de ambiente no Azure DevOps

No pipeline, configure as seguintes variáveis:

```
DB_URL=jdbc:sqlserver://seu-servidor:1433;databaseName=greenwaydb;encrypt=true;trustServerCertificate=true
JWT_SECRET=sua-chave-secreta-aqui
RABBITMQ_HOST=seu-rabbitmq-host
RABBITMQ_USERNAME=seu-usuario
RABBITMQ_PASSWORD=sua-senha
```

### 2. Ativar o perfil de produção

No `application.properties` ou via variável de ambiente:

```properties
spring.profiles.active=prod
```

### 3. Configuração do Flyway em produção

O `application-prod.properties` já está configurado com:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-migration-naming=true
spring.flyway.locations=classpath:db/migration
spring.jpa.hibernate.ddl-auto=none
```

## 📝 Criando novas migrations

### Nomenclatura
As migrations devem seguir o padrão:
```
V{versão}__{descricao}.sql
```

Exemplos:
- `V4__create_patio.sql`
- `V5__add_index_to_users.sql`
- `V6__alter_endereco_add_cidade.sql`

### Exemplo de migration

```sql
-- V4__create_patio.sql
CREATE TABLE patio (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    localizacao VARCHAR(255)
);
```

## ⚠️ Importante

1. **Nunca altere migrations já executadas** - Crie uma nova migration para alterações
2. **Ordem das migrations** - O Flyway executa em ordem numérica (V1, V2, V3...)
3. **Testes** - Flyway está desabilitado nos testes para não interferir
4. **Desenvolvimento** - Flyway está desabilitado no perfil `dev` (usa H2)

## 🔍 Verificando migrations

O Flyway cria automaticamente a tabela `flyway_schema_history` no banco de dados para rastrear quais migrations foram executadas.

## 📚 Referências

- [Documentação do Flyway](https://flywaydb.org/documentation/)
- [Spring Boot + Flyway](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)

