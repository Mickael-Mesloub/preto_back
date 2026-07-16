package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(ApiCode code, String message) {
        super(code, message, HttpStatus.NOT_FOUND);
    }
}
