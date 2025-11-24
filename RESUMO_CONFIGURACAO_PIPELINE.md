# Resumo - Configuração do Pipeline Azure DevOps (Editor Clássico)

## ⚡ Configuração Rápida

### Tarefa Maven - Configuração Mínima

1. **Maven POM file**: `pom.xml`
2. **Goals**: `clean package`
3. **Options**: `-DskipTests=false -Dspring.profiles.active=test`
4. **JDK version**: `JDK 17`
5. **Publish JUnit results**: ☑ Sim
6. **Test results files**: `**/surefire-reports/TEST-*.xml`

## 🔧 Se Ainda Falhar

### Opção 1: Compilar sem Testes (Temporário)

Use esta configuração para verificar se pelo menos a compilação funciona:

```
Goals: clean compile
Options: -DskipTests
```

### Opção 2: Ver Logs Detalhados

Adicione `-X` nas opções para ver logs completos:

```
Goals: clean package
Options: -DskipTests=false -Dspring.profiles.active=test -X
```

### Opção 3: Testar Localmente Primeiro

Execute no seu computador:

```bash
mvn clean test
```

Se falhar localmente, o pipeline também vai falhar. Corrija os problemas localmente primeiro.

## 📋 Checklist Antes de Executar Pipeline

- [ ] Código compila localmente: `mvn clean compile`
- [ ] Testes passam localmente: `mvn clean test`
- [ ] Arquivo `pom.xml` está na raiz do projeto
- [ ] Arquivo `src/test/java/com/greenway/greenway/GreenWayApplicationTests.java` existe
- [ ] Arquivo `src/test/resources/application-test.properties` existe
- [ ] JDK 17 está configurado na tarefa Maven

## 🐛 Debugging

### Ver Logs Completos no Azure DevOps

1. Execute o pipeline
2. Quando falhar, clique na tarefa **Maven**
3. Clique em **"View logs"**
4. Procure por `[ERROR]` ou `[WARNING]`
5. Copie a mensagem de erro completa

### Erros Comuns

| Erro | Causa Provável | Solução |
|------|----------------|---------|
| `pom.xml not found` | Caminho incorreto | Use apenas `pom.xml` |
| `Java version mismatch` | JDK errado | Configure JDK 17 |
| `Tests failed` | Teste falhando | Veja logs do Surefire |
| `Out of memory` | Memória insuficiente | Adicione `-Xmx2048m` nas opções |
| `Compilation failure` | Erro de código | Corrija o código Java |

## 📁 Arquivos Importantes

Certifique-se de que estes arquivos existem:

```
✅ pom.xml (raiz)
✅ src/test/java/com/greenway/greenway/GreenWayApplicationTests.java
✅ src/test/resources/application-test.properties
```

## 🚀 Próximos Passos

1. Configure a tarefa Maven conforme acima
2. Execute o pipeline
3. Se falhar, verifique os logs
4. Se necessário, use `clean compile -DskipTests` temporariamente
5. Corrija os problemas encontrados
6. Re-execute

## 📞 Precisa de Mais Ajuda?

Se ainda tiver problemas:
1. Execute `mvn clean test -X` localmente
2. Copie o erro completo
3. Verifique se o erro é o mesmo no Azure DevOps
4. Consulte `MAVEN_TASK_CONFIGURATION.md` para mais detalhes

