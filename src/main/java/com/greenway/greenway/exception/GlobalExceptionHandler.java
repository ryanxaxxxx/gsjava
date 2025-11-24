package com.greenway.greenway.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Handle @Valid errors (DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex, Locale locale) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

    // Handle @Valid errors em parâmetros e path variables
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(cv -> {
            errors.put(cv.getPropertyPath().toString(), cv.getMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    // Handle not found (quando service usa orElseThrow)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException ex, Locale locale) {

        String msg = messageSource.getMessage(
                "resource.notfound",
                null,
                "Recurso não encontrado",
                locale
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }

    // Handle TypeMismatchException (erro de conversão de tipos)
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(TypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro de conversão de tipo");
        error.put("message", "Não foi possível converter o valor '" + ex.getValue() + 
                "' para o tipo esperado: " + ex.getRequiredType().getSimpleName());
        error.put("field", ex.getPropertyName() != null ? ex.getPropertyName() : "desconhecido");
        return ResponseEntity.badRequest().body(error);
    }

    // Handle NoResourceFoundException (favicon, etc) - ignorar silenciosamente
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        // Ignorar erros de recursos estáticos não encontrados (como favicon.ico)
        return ResponseEntity.notFound().build();
    }

    // Handle HttpMediaTypeNotSupportedException
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Tipo de conteúdo não suportado");
        error.put("message", "O tipo de conteúdo '" + ex.getContentType() + "' não é suportado. Use 'application/x-www-form-urlencoded' ou 'application/json'.");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    // Fallback geral + tradução de mensagens como user.email.duplicate
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex, Locale locale) {
        
        // Log do erro completo para debug
        ex.printStackTrace();

        String messageKey = ex.getMessage();
        String msg;

        // Se for chave do messages.properties
        if (messageKey != null && messageKey.startsWith("user.")) {
            msg = messageSource.getMessage(messageKey, null, messageKey, locale);
        } else {
            msg = messageSource.getMessage(
                    "internal.error",
                    null,
                    "Erro interno no servidor",
                    locale
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
}
