# Troubleshooting Flyway - Migrations Não Executam

## 🔍 Verificações Básicas

### 1. Verificar se as migrations estão no JAR

Após o build, verifique se os arquivos SQL estão incluídos:

```bash
# Extrair e verificar o JAR
jar -tf target/green_way-0.0.1-SNAPSHOT.jar | grep "db/migration"
```

Você deve ver:
```
BOOT-INF/classes/db/migration/V1__create_usuario.sql
BOOT-INF/classes/db/migration/V2__create_users.sql
BOOT-INF/classes/db/migration/V3__create_endereco.sql
```

### 2. Verificar Logs do Flyway

Quando a aplicação inicia, procure por logs do Flyway:

```
Flyway Community Edition 11.14.1 by Redgate
Database: jdbc:sqlserver://...
Successfully validated 3 migrations (execution time 00:00.123s)
Creating Schema History table [greenwaydb].[dbo].[flyway_schema_history] ...
Current version of schema [dbo]: << Empty Schema >>
Migrating schema [dbo] to version "1 - create usuario"
Migrating schema [dbo] to version "2 - create users"
Migrating schema [dbo] to version "3 - create endereco"
Successfully applied 3 migrations to schema [dbo] (execution time 00:00.456s)
```

## ⚠️ Problemas Comuns

### Problema 1: Flyway não encontra as migrations

**Sintomas:**
- Logs mostram "No migrations found"
- Tabelas não são criadas

**Soluções:**

1. **Verificar localização das migrations:**
   - Deve estar em: `src/main/resources/db/migration/`
   - Nomes devem seguir o padrão: `V{version}__{description}.sql`

2. **Verificar se estão sendo incluídas no build:**
   - Execute: `mvn clean package`
   - Verifique o JAR gerado

3. **Verificar configuração no application.properties:**
   ```properties
   spring.flyway.locations=classpath:db/migration
   ```

### Problema 2: Flyway não executa (silenciosamente)

**Sintomas:**
- Aplicação inicia sem erros
- Mas as tabelas não são criadas
- Não há logs do Flyway

**Soluções:**

1. **Verificar se Flyway está habilitado:**
   ```properties
   spring.flyway.enabled=true
   ```

2. **Verificar conexão com banco:**
   - O Flyway precisa de uma conexão válida com o banco
   - Verifique se `spring.datasource.url` está correto

3. **Verificar se há erro silencioso:**
   - Aumente o nível de log:
   ```properties
   logging.level.org.flywaydb=DEBUG
   ```

### Problema 3: Erro "Schema history table does not exist"

**Sintomas:**
- Erro ao tentar criar tabela de histórico do Flyway

**Soluções:**

1. **Habilitar baseline:**
   ```properties
   spring.flyway.baseline-on-migrate=true
   spring.flyway.baseline-version=0
   ```

2. **Verificar permissões do usuário:**
   - O usuário precisa de permissão para criar tabelas
   - Execute no SQL Server:
   ```sql
   ALTER ROLE db_owner ADD MEMBER seu-usuario;
   ```

### Problema 4: Migrations executam mas tabelas não aparecem

**Sintomas:**
- Logs mostram migrations executadas
- Mas tabelas não existem no banco

**Soluções:**

1. **Verificar schema:**
   ```properties
   spring.jpa.properties.hibernate.default_schema=dbo
   spring.flyway.schemas=dbo
   ```

2. **Verificar se está olhando o banco correto:**
   - Confirme que a URL do banco está correta
   - Verifique se está conectando ao banco certo

3. **Verificar se há rollback:**
   - Verifique os logs para erros após a execução das migrations

## 🔧 Configuração Recomendada

### application.properties

```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-migration-naming=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-version=0
spring.flyway.baseline-description=Initial baseline
spring.flyway.clean-disabled=true
spring.flyway.out-of-order=false
spring.flyway.schemas=dbo

# Logging para debug
logging.level.org.flywaydb=INFO
```

### Verificar Tabelas Criadas

Execute no SQL Server:

```sql
-- Verificar tabelas criadas
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo';

-- Verificar histórico do Flyway
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

## 🚀 Teste Local

Para testar se o Flyway funciona localmente:

1. **Configure o banco local:**
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=greenwaydb;encrypt=true;trustServerCertificate=true
   spring.datasource.username=seu-usuario
   spring.datasource.password=sua-senha
   ```

2. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

3. **Verifique os logs** para mensagens do Flyway

4. **Verifique o banco** para ver se as tabelas foram criadas

## 📋 Checklist de Verificação

- [ ] Migrations estão em `src/main/resources/db/migration/`
- [ ] Nomes seguem padrão `V{version}__{description}.sql`
- [ ] `spring.flyway.enabled=true` no application.properties
- [ ] `spring.flyway.locations=classpath:db/migration` configurado
- [ ] Conexão com banco está funcionando
- [ ] Usuário tem permissões para criar tabelas
- [ ] Migrations estão sendo incluídas no JAR (verificar com `jar -tf`)
- [ ] Logs do Flyway aparecem na inicialização
- [ ] Tabela `flyway_schema_history` existe no banco
- [ ] Tabelas `users` e `endereco` existem no banco

## 🔍 Comandos Úteis

### Verificar conteúdo do JAR:
```bash
jar -tf target/green_way-0.0.1-SNAPSHOT.jar | grep -i migration
```

### Verificar logs do Flyway:
```bash
# No Azure App Service
az webapp log tail --name seu-app --resource-group seu-rg | grep -i flyway
```

### Verificar tabelas no SQL Server:
```sql
USE greenwaydb;
SELECT name FROM sys.tables WHERE schema_id = SCHEMA_ID('dbo');
```

### Limpar histórico do Flyway (CUIDADO - apenas para desenvolvimento):
```sql
-- APENAS PARA DESENVOLVIMENTO - NÃO USE EM PRODUÇÃO
DROP TABLE IF EXISTS flyway_schema_history;
```

## 📞 Próximos Passos

Se o problema persistir:

1. **Ative logs detalhados:**
   ```properties
   logging.level.org.flywaydb=DEBUG
   logging.level.org.springframework.boot.autoconfigure.flyway=DEBUG
   ```

2. **Verifique os logs completos** do Azure App Service

3. **Teste localmente** com as mesmas configurações

4. **Verifique se há erros de SQL** nas migrations (sintaxe SQL Server)

