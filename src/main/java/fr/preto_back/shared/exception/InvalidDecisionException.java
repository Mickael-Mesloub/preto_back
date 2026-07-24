package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class InvalidDecisionException extends ApiException {
    public InvalidDecisionException() {
        super(ApiCode.RESA_INVALID_DECISION, ApiCode.RESA_INVALID_DECISION.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
