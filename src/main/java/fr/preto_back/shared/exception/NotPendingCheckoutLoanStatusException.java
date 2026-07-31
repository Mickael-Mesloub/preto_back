package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class NotPendingCheckoutLoanStatusException extends ApiException {
    public NotPendingCheckoutLoanStatusException() {
        super(ApiCode.LOAN_NOT_PENDING_CHECKOUT, ApiCode.LOAN_NOT_PENDING_CHECKOUT.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
