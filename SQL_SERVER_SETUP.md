# Configuração do SQL Server para GreenWay

## Pré-requisitos

1. SQL Server instalado e rodando (versão 2017 ou superior)
2. SQL Server Management Studio (SSMS) ou Azure Data Studio (opcional, para gerenciamento)

## Passo 1: Criar o Banco de Dados

Execute o seguinte comando SQL no SQL Server:

```sql
CREATE DATABASE greenwaydb;
GO

USE greenwaydb;
GO
```

## Passo 2: Configurar Credenciais

### Opção A: Usar autenticação do Windows (recomendado para desenvolvimento local)

No `application.properties`, configure:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=greenwaydb;encrypt=true;trustServerCertificate=true;integratedSecurity=true
spring.datasource.username=
spring.datasource.password=
```

**Nota:** Para usar autenticação integrada do Windows, você precisa:
1. Adicionar o driver `sqljdbc_auth.dll` ao classpath (geralmente em `sqljdbc_X.X/enu/auth/x64/`)
2. Ou usar o driver JDBC mais recente que suporta autenticação integrada

### Opção B: Usar autenticação SQL Server (recomendado para produção/cloud)

1. Crie um usuário SQL Server:

```sql
USE greenwaydb;
GO

CREATE LOGIN greenway_user WITH PASSWORD = 'SuaSenhaSegura123!';
GO

CREATE USER greenway_user FOR LOGIN greenway_user;
GO

ALTER ROLE db_owner ADD MEMBER greenway_user;
GO
```

2. Configure no `application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=greenwaydb;encrypt=true;trustServerCertificate=true
spring.datasource.username=greenway_user
spring.datasource.password=SuaSenhaSegura123!
```

## Passo 3: Configurar para Azure SQL Database (Cloud)

Se estiver usando Azure SQL Database, atualize a URL de conexão:

```properties
spring.datasource.url=jdbc:sqlserver://seu-servidor.database.windows.net:1433;databaseName=greenwaydb;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30
spring.datasource.username=seu-usuario@seu-servidor
spring.datasource.password=SuaSenhaSegura123!
```

## Passo 4: Habilitar TCP/IP no SQL Server

1. Abra o **SQL Server Configuration Manager**
2. Vá em **SQL Server Network Configuration** > **Protocols for [INSTANCE_NAME]**
3. Habilite **TCP/IP**
4. Clique com botão direito em **TCP/IP** > **Properties**
5. Na aba **IP Addresses**, configure:
   - **IPAll**: Porta TCP = 1433
6. Reinicie o serviço SQL Server

## Passo 5: Configurar Firewall

### Para SQL Server local:
- Permita conexões na porta 1433 no Windows Firewall

### Para Azure SQL Database:
- Adicione seu IP público nas regras de firewall do Azure Portal
- Ou habilite "Allow Azure services and resources to access this server"

## Perfis de Ambiente

### Desenvolvimento Local (H2)
Para usar H2 em memória durante desenvolvimento:

```bash
# No IDE (Eclipse/IntelliJ), configure:
spring.profiles.active=dev
```

Ou adicione no `application.properties`:
```properties
spring.profiles.active=dev
```

### Produção/Cloud (SQL Server)
O perfil padrão usa SQL Server. Certifique-se de que as credenciais estão configuradas corretamente.

## Verificação

Após configurar, inicie a aplicação. O Spring Boot irá:
1. Conectar ao SQL Server
2. Criar automaticamente as tabelas (devido a `spring.jpa.hibernate.ddl-auto=update`)

Para verificar se está funcionando, verifique os logs da aplicação. Você deve ver:
```
Hibernate: create table endereco ...
Hibernate: create table users ...
```

## Troubleshooting

### Erro: "Cannot connect to SQL Server"
- Verifique se o SQL Server está rodando
- Verifique se a porta 1433 está acessível
- Verifique as credenciais no `application.properties`

### Erro: "Login failed for user"
- Verifique se o usuário existe e tem permissões
- Verifique se a senha está correta

### Erro: "Database 'greenwaydb' does not exist"
- Execute o comando `CREATE DATABASE greenwaydb;` no SQL Server

### Para desenvolvimento rápido (usar H2 temporariamente)
Se precisar testar rapidamente sem configurar SQL Server, use o perfil dev:
```properties
spring.profiles.active=dev
```

