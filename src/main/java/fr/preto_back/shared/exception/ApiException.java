package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private final ApiCode code;
    private final HttpStatus status;

    public ApiException(ApiCode code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }
}