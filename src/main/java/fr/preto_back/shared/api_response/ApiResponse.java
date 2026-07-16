package fr.preto_back.shared.api_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    public String code;
    public String message;
    public T data;
    public List<ApiValidationError> errors;

    // Success response with data
    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .data(data)
                .errors(null)
                .build();
    }

    // Validation error(s) response with field and code
    public static <T> ApiResponse<T> validationError(List<ApiValidationError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(ApiCode.VALIDATION_ERROR.name())
                .message(ApiCode.VALIDATION_ERROR.getMessage())
                .data(null)
                .errors(errors)
                .build();
    }

    // Other types of error
    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .errors(null)
                .build();
    }
}
