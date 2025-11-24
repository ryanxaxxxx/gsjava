# Configuração do Pipeline - Editor Clássico do Azure DevOps

## Configuração Passo a Passo

### 1. Agent Job
- **Agent Specification**: `windows-latest` ✅ (você já configurou)
- **Display name**: "Build and Test"

### 2. Tarefa: Maven (Task #4)

Configure exatamente assim:

#### Aba "Maven POM file"
- **Maven POM file**: `pom.xml`
- ✅ Marque "Use default Maven version"

#### Aba "Options"
- **Goals**: `clean package`
- **Options**: `-DskipTests=false -Dspring.profiles.active=test -X`
  - O `-X` é para debug. Depois que funcionar, pode remover.

#### Aba "JUnit Test Results"
- ✅ Marque "Publish to Azure Pipelines"
- **Test results files**: `**/surefire-reports/TEST-*.xml`
- **Test run title**: `Test Results`

#### Aba "Code Coverage"
- Deixe desmarcado por enquanto

#### Aba "Advanced"
- **JDK version**: `JDK 17` ✅ (você já configurou)
- **JDK architecture**: `x64`
- **Maven options**: Deixe vazio ou `-Xmx2048m` se houver problemas de memória

#### Aba "Control Options"
- ✅ Marque "Only when all previous jobs have succeeded" ✅ (você já configurou)

### 3. Tarefa: Publish Build Artifacts

- **Path to publish**: `target/*.jar`
- **Artifact name**: `drop`
- ✅ Marque "Only when all previous jobs have succeeded"

## Configuração Alternativa (Se ainda falhar)

### Opção A: Compilar sem testes primeiro

Crie DUAS tarefas Maven:

**Tarefa 1: Compile**
- Goals: `clean compile`
- Options: `-DskipTests`

**Tarefa 2: Test**
- Goals: `test`
- Options: `-Dspring.profiles.active=test`

### Opção B: Pular testes temporariamente (apenas para debug)

Na tarefa Maven:
- Goals: `clean package`
- Options: `-DskipTests`

⚠️ **ATENÇÃO**: Isso não executa testes. Use apenas para verificar se a compilação funciona.

## Verificações Importantes

### 1. Verificar se o pom.xml está na raiz
O arquivo `pom.xml` deve estar na raiz do repositório, não em uma subpasta.

### 2. Verificar estrutura de pastas
```
projeto/
  ├── pom.xml
  ├── src/
  │   ├── main/
  │   │   └── java/
  │   └── test/
  │       ├── java/
  │       │   └── com/
  │       │       └── greenway/
  │       │           └── greenway/
  │       │               └── GreenWayApplicationTests.java
  │       └── resources/
  │           └── application-test.properties
```

### 3. Verificar logs do Maven
Se o build falhar, clique em "View logs" na tarefa Maven e procure por:
- `[ERROR]` - Erros específicos
- `[WARNING]` - Avisos que podem causar problemas
- Stack traces completos

## Comandos para Testar Localmente

Antes de fazer commit, teste localmente:

```bash
# Teste completo
mvn clean test

# Se falhar, teste com debug
mvn clean test -X

# Se ainda falhar, teste apenas compilação
mvn clean compile
```

## Problemas Comuns e Soluções

### Erro: "pom.xml not found"
- **Causa**: O Maven não está encontrando o pom.xml
- **Solução**: Verifique o caminho no campo "Maven POM file". Deve ser apenas `pom.xml` se estiver na raiz.

### Erro: "Java version mismatch"
- **Causa**: Versão do Java incorreta
- **Solução**: Certifique-se de que está usando JDK 17

### Erro: "Tests failed"
- **Causa**: Testes falhando
- **Solução**: Veja a seção de troubleshooting abaixo

### Erro: "Out of memory"
- **Causa**: Memória insuficiente
- **Solução**: Adicione `-Xmx2048m` nas opções do Maven

## Próximos Passos

1. Configure a tarefa Maven conforme acima
2. Execute o pipeline
3. Se falhar, verifique os logs detalhados
4. Se necessário, use a Opção B temporariamente para verificar compilação

