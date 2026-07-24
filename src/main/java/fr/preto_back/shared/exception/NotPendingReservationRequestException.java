package fr.preto_back.shared.exception;

import fr.preto_back.shared.api_response.ApiCode;
import org.springframework.http.HttpStatus;

public class NotPendingReservationRequestException extends ApiException {
    public NotPendingReservationRequestException() {
        super(ApiCode.RESA_NOT_PENDING, ApiCode.RESA_NOT_PENDING.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
