package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class DeclineReasonMissingException extends ApiException {
    public DeclineReasonMissingException() {
        super(ApiCode.RESA_DECLINED_MISSING_REASON, ApiCode.RESA_DECLINED_MISSING_REASON.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
