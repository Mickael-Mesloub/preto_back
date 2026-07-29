package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import fr.preto_back.shared.api_response.ApiValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles custom api exceptions
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        log.warn("API exception occurred: code={}, status={}, message={}", e.getCode(), e.getStatus(), e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.error(e.getCode().name(), e.getMessage()));
    }

    // Handles no body provided or invalid body structure errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.error(
                        ApiCode.INVALID_REQUEST_BODY.name(),
                        ApiCode.INVALID_REQUEST_BODY.getMessage()
                )
        );
    }

    // Handles validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<ApiValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        errors.forEach(error -> {
            log.warn("Validation failed - field={}, validationErrorMessageKey={}", error.getField(), error.getValidationErrorMessageKey());
        });

        return ResponseEntity.badRequest().body(
                ApiResponse.validationError(errors)
        );
    }

    // Handles invalid/unknown routes (e.g : /catalogg instead of /catalog)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRouteNotFound(NoResourceFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ApiCode.ROUTE_NOT_FOUND.name(), ApiCode.ROUTE_NOT_FOUND.getMessage()));
    }

    // Fallback handler for unexpected errors. Should not be explicitly used in code.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ApiCode.INTERNAL_SERVER_ERROR.name(), ApiCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
