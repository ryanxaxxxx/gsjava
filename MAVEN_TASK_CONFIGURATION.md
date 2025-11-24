# Configuração Detalhada da Tarefa Maven - Editor Clássico

## Configuração Exata da Tarefa Maven

### Passo 1: Adicionar Tarefa Maven
1. No editor clássico, clique em **"+ Add a task"**
2. Procure por **"Maven"**
3. Selecione **"Maven"** (versão 3 ou superior)
4. Clique em **"Add"**

### Passo 2: Configurar a Tarefa

#### Aba "Maven POM file"
```
Maven POM file: pom.xml
☑ Use default Maven version
```

#### Aba "Options"
```
Goals: clean package
Options: -DskipTests=false -Dspring.profiles.active=test
```

**OU** se quiser ver logs detalhados (para debug):
```
Goals: clean package
Options: -DskipTests=false -Dspring.profiles.active=test -X
```

#### Aba "JUnit Test Results"
```
☑ Publish to Azure Pipelines
Test results files: **/surefire-reports/TEST-*.xml
Test run title: Test Results
```

#### Aba "Advanced"
```
JDK version: JDK 17
JDK architecture: x64
Maven options: (deixe vazio ou -Xmx2048m se houver problemas de memória)
```

#### Aba "Control Options"
```
☑ Only when all previous jobs have succeeded
```

## Alternativa: Compilar sem Testes (Temporário)

Se os testes continuarem falhando, use esta configuração temporária para pelo menos compilar:

### Tarefa Maven - Compilação
```
Goals: clean compile
Options: -DskipTests
```

Isso vai:
- ✅ Compilar o código
- ✅ Verificar se há erros de compilação
- ❌ Não executar testes

## Verificar Logs de Erro

Após executar o pipeline:

1. Clique na tarefa **Maven** que falhou
2. Expanda **"View logs"**
3. Procure por linhas que começam com `[ERROR]`
4. Copie o erro completo

### Erros Comuns

#### Erro: "MojoExecutionException"
- Geralmente indica problema na execução de um plugin
- Verifique se todas as dependências estão corretas no pom.xml

#### Erro: "Compilation failure"
- Erro de compilação Java
- Verifique se o código compila localmente

#### Erro: "Tests failed"
- Testes falhando
- Verifique os logs do Surefire em `target/surefire-reports/`

## Teste Local Antes de Commitar

Execute estes comandos localmente antes de fazer commit:

```bash
# 1. Limpar e compilar
mvn clean compile

# 2. Se compilou, testar
mvn test

# 3. Se testou, fazer build completo
mvn clean package
```

Se algum desses comandos falhar localmente, o pipeline também vai falhar.

## Estrutura do Projeto Esperada

Certifique-se de que a estrutura está assim:

```
Green-way/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── greenway/
│   │   │           └── greenway/
│   │   │               └── GreenWayApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── greenway/
│       │           └── greenway/
│       │               └── GreenWayApplicationTests.java
│       └── resources/
│           └── application-test.properties
```

## Próximos Passos

1. ✅ Configure a tarefa Maven conforme acima
2. ✅ Execute o pipeline
3. ✅ Se falhar, verifique os logs
4. ✅ Se necessário, use compilação sem testes temporariamente
5. ✅ Corrija os problemas encontrados nos logs
6. ✅ Re-execute o pipeline

