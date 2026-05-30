package com.chs.cyber_humanity_service.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Convierte esta clase en la red de seguridad de todos los controladores
@Slf4j // Nos permite usar logs
public class GlobalExceptionHandler {

    // 1. CAPTURADOR MULTIERRORES DE VALIDACIÓN (Faltan campos, negativos, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> manejarValidacionesDeModelo(MethodArgumentNotValidException ex) {

        // Creamos un mapa para guardar: "campo" -> "mensaje de error"
        Map<String, String> detallesDeErrores = new HashMap<>();

        // Recorremos absolutamente todas las validaciones que fallaron en el JSON
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detallesDeErrores.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("--- [RECHAZADO] El JSON enviado violó {} reglas de validación ---", detallesDeErrores.size());

        // Construimos la respuesta enviando el mapa en el nuevo campo 'detalles'
        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error en los datos enviados. Revisa los campos señalados.",
                detallesDeErrores
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 2. CAPTURADOR DE ERRORES CONTROLADOS (Lanzados en el Service: Stock, 404)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> manejarErroresDeNegocio(ResponseStatusException ex) {
        String mensajeUsuario = ex.getReason();
        HttpStatus estadoHttp = HttpStatus.valueOf(ex.getStatusCode().value());

        log.warn("--- [RECHAZADO] Error de negocio controlado: {} ---", mensajeUsuario);

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                estadoHttp.value(),
                estadoHttp.getReasonPhrase(),
                mensajeUsuario,
                null // No aplica mapa de detalles para errores lógicos individuales
        );

        return new ResponseEntity<>(error, estadoHttp);
    }

    // 3. CAPTURADOR DE SINTAXIS JSON (Estructura rota, letras en campos numéricos)
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> manejarJsonMalConstruido(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        log.warn("--- [RECHAZADO] Estructura JSON o tipo de dato incorrecto ---");

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Fallo en el formato de la petición. Verifica que los campos numéricos no tengan texto y que el JSON esté bien estructurado.",
                null
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 4. CAPTURADOR DE ERRORES GENÉRICOS (Fallas críticas del sistema)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> manejarErroresImprevistos(Exception ex) {
        log.error("CRÍTICO: Falla imprevista en la red del microservicio", ex);

        ErrorResponseDto error = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un fallo inesperado en los sistemas de la clínica. Por favor, intente más tarde.",
                null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // DTO DE RESPUESTA
    @Data
    @AllArgsConstructor
    public static class ErrorResponseDto {
        private LocalDateTime timestamp;
        private int codigoEstado;
        private String error;
        private String mensaje;
        // Este campo opcional guardará la lista de campos fallidos si los hay; si no, viajará como null
        private Map<String, String> detalles;
    }
}
