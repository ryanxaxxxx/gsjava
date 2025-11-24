# Configuração Flyway + SQL Server - GreenWay

## ✅ Alterações Realizadas

### 1. POM.xml
- ✅ **Flyway versão específica**: 11.14.1 (igual ao projeto que funcionou)
- ✅ **flyway-sqlserver**: Adicionada dependência específica para SQL Server
- ✅ **H2 removido** da produção: Agora apenas `scope=test` (só para testes)
- ✅ **PostgreSQL removido**: Não é mais necessário
- ✅ **maven-resources-plugin**: Adicionado para garantir encoding UTF-8

### 2. application.properties
- ✅ **SQL Server como padrão**: Configurado diretamente (sem perfil dev)
- ✅ **Flyway habilitado**: Todas as configurações necessárias
- ✅ **ddl-auto=none**: Flyway cuida das migrations
- ✅ **DB_URL via variável de ambiente**: `${DB_URL}`

### 3. Migrations
- ✅ **V1__create_usuario.sql**: Drop das tabelas existentes (baseline limpo)
- ✅ **V2__create_users.sql**: Tabela users criada
- ✅ **V3__create_endereco.sql**: Tabela endereco criada
- ✅ Localização: `src/main/resources/db/migration/`

### 4. Perfis
- ✅ **application.properties**: SQL Server + Flyway (padrão)
- ✅ **application-test.properties**: H2 para testes (Flyway desabilitado)
- ✅ **application-prod.properties**: SQL Server + Flyway (para Azure DevOps)
- ❌ **application-dev.properties**: Removido (não usa mais H2)

## 📋 Configuração no Azure DevOps

### Variáveis de Ambiente Necessárias

No pipeline do Azure DevOps, configure:

```
DB_URL=jdbc:sqlserver://seu-servidor:1433;databaseName=greenwaydb;encrypt=true;trustServerCertificate=true
```

### Perfil de Produção (Opcional)

Se quiser usar o perfil `prod` explicitamente, adicione:

```
spring.profiles.active=prod
```

Mas não é necessário, pois o `application.properties` já está configurado para SQL Server.

## 🔍 Verificação

### Como verificar se as tabelas foram criadas:

1. **Logs da aplicação**: Procure por mensagens do Flyway:
   ```
   Flyway Community Edition 11.14.1 by Redgate
   Database: jdbc:sqlserver://...
   Successfully validated 3 migrations
   Current version of schema [dbo]: << Empty Schema >>
   Migrating schema [dbo] to version "1 - create usuario"
   Migrating schema [dbo] to version "2 - create users"
   Migrating schema [dbo] to version "3 - create endereco"
   Successfully applied 3 migrations
   ```

2. **Tabela flyway_schema_history**: O Flyway cria esta tabela automaticamente para rastrear migrations executadas.

3. **Tabelas criadas**:
   - `users`
   - `endereco`
   - `flyway_schema_history` (criada automaticamente pelo Flyway)

## ⚠️ Importante

1. **DB_URL deve estar configurado**: Sem essa variável, a aplicação não conseguirá conectar ao banco
2. **Flyway executa na inicialização**: As migrations são executadas automaticamente quando a aplicação inicia
3. **ddl-auto=none**: O Hibernate não cria/atualiza tabelas, apenas o Flyway
4. **Testes usam H2**: Os testes continuam usando H2 em memória (Flyway desabilitado)

## 🚀 Próximos Passos

1. ✅ Commit das alterações
2. ✅ Configurar `DB_URL` no Azure DevOps
3. ✅ Executar o pipeline
4. ✅ Verificar logs para confirmar que as migrations foram executadas
5. ✅ Verificar no banco SQL Server se as tabelas foram criadas

## 📝 Estrutura Final

```
src/main/resources/
├── application.properties          (SQL Server + Flyway - padrão)
├── application-prod.properties     (SQL Server + Flyway - produção)
├── application-test.properties     (H2 - testes)
└── db/
    └── migration/
        ├── V1__create_usuario.sql
        ├── V2__create_users.sql
        └── V3__create_endereco.sql
```

