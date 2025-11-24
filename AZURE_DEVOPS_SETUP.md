# Configuração do Azure DevOps Pipeline para GreenWay

## Problemas Resolvidos

✅ **Teste corrigido**: Movido para o pacote correto (`com.greenway.greenway`)
✅ **Perfil de teste criado**: `application-test.properties` com H2 em memória
✅ **Maven Surefire configurado**: Plugin configurado corretamente no `pom.xml`
✅ **Dependências de teste**: RabbitMQ e outras dependências desabilitadas durante testes

## Configuração do Pipeline no Azure DevOps

### Opção 1: Usar o arquivo YAML (Recomendado)

1. No Azure DevOps, vá em **Pipelines** > **New Pipeline**
2. Selecione seu repositório
3. Escolha **Existing Azure Pipelines YAML file**
4. Selecione o arquivo `azure-pipelines.yml` na raiz do projeto
5. Salve e execute

### Opção 2: Criar Pipeline Manualmente

1. No Azure DevOps, vá em **Pipelines** > **New Pipeline**
2. Selecione seu repositório
3. Escolha **Starter pipeline** ou **Maven**
4. Configure as seguintes tarefas:

#### Tarefa 1: Java Tool Installer
```yaml
- task: JavaToolInstaller@0
  inputs:
    versionSpec: '17'
    jdkArchitecture: 'x64'
```

#### Tarefa 2: Maven Build
```yaml
- task: Maven@3
  inputs:
    mavenPomFile: 'pom.xml'
    goals: 'clean package'
    options: '-DskipTests=false'
    publishJUnitResults: true
    testResultsFiles: '**/surefire-reports/TEST-*.xml'
```

## Estrutura de Testes

### Arquivos de Teste
- **Teste Principal**: `src/test/java/com/greenway/greenway/GreenWayApplicationTests.java`
- **Configuração de Teste**: `src/test/resources/application-test.properties`

### Perfil de Teste
O perfil `test` usa:
- **Banco de dados**: H2 em memória (não precisa de SQL Server)
- **RabbitMQ**: Desabilitado (auto-config excluído)
- **Segurança**: Configuração simplificada
- **Web Environment**: NONE (não inicia servidor web)

## Comandos Maven Úteis

### Executar testes localmente
```bash
mvn clean test
```

### Build sem testes (não recomendado para CI/CD)
```bash
mvn clean package -DskipTests
```

### Build com testes (padrão)
```bash
mvn clean package
```

## Troubleshooting

### Erro: "Tests failed"
- Verifique se o perfil `test` está sendo usado
- Certifique-se de que `application-test.properties` existe
- Verifique os logs do Maven Surefire em `target/surefire-reports/`

### Erro: "Cannot connect to database"
- O perfil de teste usa H2 em memória, não precisa de conexão externa
- Verifique se `application-test.properties` está correto

### Erro: "RabbitMQ connection failed"
- O RabbitMQ está desabilitado no perfil de teste
- Verifique se `spring.autoconfigure.exclude` está configurado

### Erro: "Package com.example.GreenWay not found"
- O teste foi movido para `com.greenway.greenway`
- Certifique-se de que o arquivo antigo foi removido

## Variáveis de Ambiente no Pipeline

Se precisar configurar variáveis específicas para o pipeline:

```yaml
variables:
  MAVEN_OPTS: '-Xmx2048m'
  JAVA_HOME: '$(JAVA_HOME_17_X64)'
```

## Cache do Maven (Opcional)

Para acelerar builds subsequentes, adicione cache:

```yaml
- task: Cache@2
  inputs:
    key: 'maven | "$(Agent.OS)" | pom.xml'
    restoreKeys: |
      maven | "$(Agent.OS)"
    path: '$(MAVEN_CACHE_FOLDER)'
```

## Próximos Passos

1. ✅ Teste o pipeline localmente: `mvn clean test`
2. ✅ Configure o pipeline no Azure DevOps
3. ✅ Execute o pipeline e verifique os resultados
4. ✅ Configure deploy automático (opcional)

## Notas Importantes

- Os testes usam H2 em memória, então não precisam de SQL Server configurado
- O perfil `test` é ativado automaticamente durante `mvn test`
- Certifique-se de que todas as dependências estão no `pom.xml`
- O Maven Surefire está configurado para publicar resultados JUnit no Azure DevOps

