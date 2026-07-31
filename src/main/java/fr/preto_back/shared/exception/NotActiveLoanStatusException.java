package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class NotActiveLoanStatusException extends ApiException {
    public NotActiveLoanStatusException() {
        super(ApiCode.LOAN_NOT_ACTIVE, ApiCode.LOAN_NOT_ACTIVE.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
