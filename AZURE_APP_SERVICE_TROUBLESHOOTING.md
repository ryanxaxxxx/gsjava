# Guia de Troubleshooting - Azure App Service

## 🔴 Erro: "Application Error" ou "Error 403 - This web app is stopped"

### Problema 1: App Service Pausado Automaticamente

**Causa:** Planos Free/Shared do Azure App Service pausam automaticamente após 20 minutos de inatividade.

**Solução:**

1. **Verificar o Plano de Hospedagem:**
   - No Azure Portal, vá em **App Service** > **Configuration** > **General settings**
   - Verifique o **App Service Plan**
   - Se for **Free** ou **Shared**, considere atualizar para **Basic** ou superior

2. **Iniciar o App Service Manualmente:**
   - No Azure Portal, vá em **App Service** > **Overview**
   - Clique em **Start** se estiver parado

3. **Configurar Always On (apenas para planos Basic ou superior):**
   - No Azure Portal, vá em **Configuration** > **General settings**
   - Ative **Always On**
   - Clique em **Save**

### Problema 2: Erro na Inicialização da Aplicação

**Causa:** Problemas de configuração, conexão com banco de dados, ou dependências faltando.

**Solução:**

1. **Verificar Logs do App Service:**
   ```bash
   # No Azure Portal:
   # App Service > Log stream (para logs em tempo real)
   # App Service > Logs > Application Logging (para logs históricos)
   ```

2. **Verificar Variáveis de Ambiente:**
   - No Azure Portal, vá em **Configuration** > **Application settings**
   - Verifique se as seguintes variáveis estão configuradas:
     ```
     DB_URL=jdbc:sqlserver://seu-servidor.database.windows.net:1433;database=greenwaydb;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30
     DB_USERNAME=seu-usuario@seu-servidor
     DB_PASSWORD=sua-senha
     JWT_SECRET=sua-chave-secreta
     PORT=8080
     ```

3. **Verificar Conexão com SQL Server:**
   - No Azure Portal, vá em **SQL Server** > **Firewalls and virtual networks**
   - Certifique-se de que **"Allow Azure services and resources to access this server"** está habilitado
   - Adicione o IP do App Service se necessário

### Problema 3: RabbitMQ Causando Erro

**Causa:** RabbitMQ não está disponível no Azure App Service.

**Solução:**

A aplicação já está configurada para desabilitar RabbitMQ automaticamente. Se ainda houver problemas:

1. **Verificar application.properties:**
   ```properties
   spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
   ```

2. **Se necessário, adicionar variável de ambiente no Azure:**
   ```
   SPRING_AUTOCONFIGURE_EXCLUDE=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
   ```

### Problema 4: Flyway Não Executa Migrations

**Causa:** Permissões insuficientes ou banco de dados não acessível.

**Solução:**

1. **Verificar permissões do usuário do banco:**
   ```sql
   -- Execute no SQL Server:
   ALTER ROLE db_owner ADD MEMBER seu-usuario;
   ```

2. **Verificar se o banco existe:**
   ```sql
   SELECT name FROM sys.databases WHERE name = 'greenwaydb';
   ```

3. **Verificar logs do Flyway:**
   - Procure por mensagens de erro do Flyway nos logs do App Service

## 📋 Checklist de Configuração do Azure App Service

### 1. Variáveis de Ambiente (Configuration > Application settings)

```
DB_URL=jdbc:sqlserver://seu-servidor.database.windows.net:1433;database=greenwaydb;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30
DB_USERNAME=seu-usuario@seu-servidor
DB_PASSWORD=sua-senha
JWT_SECRET=sua-chave-secreta-minima-32-caracteres
PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

### 2. Configurações do App Service (Configuration > General settings)

- ✅ **Always On**: Habilitado (se plano Basic ou superior)
- ✅ **HTTP Version**: 2.0
- ✅ **ARR Affinity**: Desabilitado (recomendado para stateless apps)

### 3. Firewall do SQL Server

- ✅ **Allow Azure services and resources to access this server**: Habilitado
- ✅ Adicionar IP do App Service se necessário

### 4. Deployment Center

- ✅ Verificar se o deployment está configurado corretamente
- ✅ Verificar se o build está sendo executado com sucesso

## 🔍 Como Verificar Logs

### Logs em Tempo Real:
1. No Azure Portal, vá em **App Service** > **Log stream**
2. Você verá logs em tempo real da aplicação

### Logs Históricos:
1. No Azure Portal, vá em **App Service** > **Logs**
2. Ative **Application Logging** e **Web server logging**
3. Configure o nível de log (Information, Warning, Error)
4. Salve e aguarde alguns minutos
5. Baixe os logs ou visualize no **Log stream**

### Logs via Kudu:
1. Acesse: `https://seu-app-service.scm.azurewebsites.net`
2. Vá em **Debug console** > **CMD** ou **PowerShell**
3. Navegue até `LogFiles/Application`

## 🚀 Comandos Úteis

### Verificar Status do App Service:
```bash
az webapp show --name seu-app-service --resource-group seu-resource-group --query state
```

### Iniciar App Service:
```bash
az webapp start --name seu-app-service --resource-group seu-resource-group
```

### Ver Logs:
```bash
az webapp log tail --name seu-app-service --resource-group seu-resource-group
```

## ⚠️ Problemas Comuns

### 1. "Cannot connect to database"
- Verifique se o firewall do SQL Server permite conexões do Azure
- Verifique se as credenciais estão corretas
- Verifique se o banco de dados existe

### 2. "Port already in use"
- O Azure App Service define automaticamente a porta via variável `PORT`
- Não configure `server.port` manualmente, use `${PORT:8080}`

### 3. "Application failed to start"
- Verifique os logs do App Service
- Verifique se todas as dependências estão disponíveis
- Verifique se o JAR foi construído corretamente

### 4. "404 Not Found" para recursos estáticos
- Verifique se os arquivos HTML estão em `src/main/resources/static/`
- Verifique se o build inclui os recursos estáticos

## 📞 Próximos Passos

Se o problema persistir:

1. **Verifique os logs detalhados** no Azure Portal
2. **Teste localmente** com as mesmas variáveis de ambiente
3. **Verifique o build** no Azure DevOps para garantir que não há erros
4. **Considere atualizar o plano** do App Service se estiver usando Free/Shared

