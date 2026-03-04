package com.operis.guard.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, Object>> handleSecurity(SecurityException ex) {
        log.warn("Unauthorized: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        String message = ex.getMessage();

        // Erros de autenticação e negócio — 401
        if (message != null && (
                message.contains("Invalid credentials") ||
                message.contains("User account is disabled") ||
                message.contains("Invalid refresh token") ||
                message.contains("Refresh token has been revoked") ||
                message.contains("Refresh token has expired") ||
                message.contains("User has no access to this company") ||
                message.contains("User access to this company is disabled") ||
                message.contains("User has no active company access")
        )) {
            log.warn("Auth error: {}", message);
            return buildResponse(HttpStatus.UNAUTHORIZED, message);
        }

        // Recursos não encontrados — 404
        if (message != null && message.contains("not found")) {
            log.warn("Not found: {}", message);
            return buildResponse(HttpStatus.NOT_FOUND, message);
        }

        // Erro interno inesperado — 500
        log.error("Unexpected error", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message != null ? message : "No message available",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
