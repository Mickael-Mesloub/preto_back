package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class NoAvailableCopyException extends ApiException {
    public NoAvailableCopyException(String message) {
        super(ApiCode.ASSET_COPY_NO_AVAILABLE_COPY, message, HttpStatus.NOT_FOUND);
    }
}
