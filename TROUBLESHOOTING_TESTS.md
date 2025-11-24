# Troubleshooting - Testes no Azure DevOps

## Se os testes ainda falharem

### Opção 1: Pular testes temporariamente (NÃO RECOMENDADO para produção)

No pipeline do Azure DevOps, adicione a opção `-DskipTests`:

```yaml
- task: Maven@3
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'clean package'
    options: '-DskipTests'  # Pula testes mas ainda compila
```

**⚠️ ATENÇÃO**: Isso não executa os testes. Use apenas para debug temporário.

### Opção 2: Verificar logs detalhados

Adicione `-X` ou `-e` nas opções do Maven para ver logs detalhados:

```yaml
options: 'clean package -X'  # Modo debug
# ou
options: 'clean package -e'  # Stack trace completo
```

### Opção 3: Executar apenas compilação

Se precisar apenas compilar sem testes:

```yaml
goals: 'clean compile'
```

## Verificações Comuns

### 1. Verificar se o perfil test está ativo
O teste deve usar o perfil `test` automaticamente. Verifique se `application-test.properties` existe em `src/test/resources/`.

### 2. Verificar dependências
Certifique-se de que todas as dependências estão no `pom.xml`:
- H2 (para testes)
- spring-boot-starter-test
- JUnit 5

### 3. Verificar Java Version
O pipeline deve usar Java 17. Verifique a configuração:

```yaml
- task: JavaToolInstaller@0
  inputs:
    versionSpec: '17'
```

### 4. Verificar memória
Se houver problemas de memória, aumente:

```yaml
options: 'clean package -Xmx2048m'
```

## Comandos para testar localmente

### Teste completo
```bash
mvn clean test
```

### Teste com logs detalhados
```bash
mvn clean test -X
```

### Teste apenas uma classe
```bash
mvn test -Dtest=GreenWayApplicationTests
```

### Compilar sem testar
```bash
mvn clean compile
```

## Estrutura de Arquivos Esperada

```
src/
  test/
    java/
      com/
        greenway/
          greenway/
            GreenWayApplicationTests.java
    resources/
      application-test.properties
```

## Problemas Conhecidos e Soluções

### Erro: "Bean creation failed"
- **Causa**: Alguma dependência não está disponível
- **Solução**: Verifique se todas as auto-configurações problemáticas estão excluídas

### Erro: "Connection refused" (RabbitMQ)
- **Causa**: RabbitMQ tentando conectar
- **Solução**: Já configurado para desabilitar no perfil test

### Erro: "JWT secret too short"
- **Causa**: JWT secret precisa ter pelo menos 32 caracteres
- **Solução**: Já configurado no application-test.properties

### Erro: "Database connection failed"
- **Causa**: Tentando conectar ao SQL Server
- **Solução**: Perfil test usa H2 em memória

## Contato

Se os problemas persistirem, verifique:
1. Logs completos do Maven no Azure DevOps
2. Arquivo `target/surefire-reports/TEST-*.xml` para detalhes do teste
3. Stack trace completo com `-e` ou `-X`

