package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class ResaNotApprovedException extends ApiException {
    public ResaNotApprovedException() {
        super(ApiCode.RESA_NOT_APPROVED, ApiCode.RESA_NOT_APPROVED.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
