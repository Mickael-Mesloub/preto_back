package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class ReturnDateIsBeforeOrEqualsStartDateException extends ApiException{
    public ReturnDateIsBeforeOrEqualsStartDateException() {
        super(ApiCode.RESA_RETURN_DATE_BEFORE_OR_EQUALS_START_DATE, ApiCode.RESA_RETURN_DATE_BEFORE_OR_EQUALS_START_DATE.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
