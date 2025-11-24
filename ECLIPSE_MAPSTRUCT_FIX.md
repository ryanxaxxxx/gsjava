# Como resolver o erro do UserMapper no Eclipse

## Problema
```
Field mapper in com.greenway.greenway.service.UserService required a bean of type 'com.greenway.greenway.mapper.UserMapper' that could not be found.
```

## Solução

### Passo 1: Atualizar o projeto Maven
1. Clique com o botão direito no projeto
2. Selecione **Maven** → **Update Project...**
3. Marque a opção **Force Update of Snapshots/Releases**
4. Clique em **OK**

### Passo 2: Limpar e recompilar
1. Clique com o botão direito no projeto
2. Selecione **Run As** → **Maven clean**
3. Depois, selecione **Run As** → **Maven compile**

### Passo 3: Verificar Source Folders
1. Clique com o botão direito no projeto
2. Selecione **Properties**
3. Vá em **Java Build Path** → **Source**
4. Verifique se `target/generated-sources/annotations` está listado
5. Se não estiver, clique em **Add Folder...** e adicione:
   - `Green-way/target/generated-sources/annotations`

### Passo 4: Habilitar Annotation Processing
1. Clique com o botão direito no projeto
2. Selecione **Properties**
3. Vá em **Java Compiler** → **Annotation Processing**
4. Marque **Enable annotation processing**
5. Clique em **Apply and Close**

### Passo 5: Recompilar o projeto
1. Pressione `Ctrl + Shift + O` para organizar imports
2. Ou faça **Project** → **Clean...** → Selecione o projeto → **Clean**

### Passo 6: Verificar se funcionou
1. Verifique se o arquivo existe em:
   - `target/generated-sources/annotations/com/greenway/greenway/mapper/UserMapperImpl.java`
2. Tente executar a aplicação novamente

## Alternativa: Usar Maven via linha de comando

Se o Eclipse continuar com problemas, você pode compilar via Maven:

```bash
mvn clean compile
```

Depois, atualize o projeto no Eclipse (F5 ou Refresh).

## Verificação

O arquivo `UserMapperImpl.java` deve estar em:
```
target/generated-sources/annotations/com/greenway/greenway/mapper/UserMapperImpl.java
```

E deve conter a anotação `@Component` para ser reconhecido pelo Spring.

