package fr.preto_back.domains.resa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReservationRequestMapper {
    public ReservationRequestResponseBody toDto(ReservationRequest reservationRequest) {
        return ReservationRequestResponseBody.builder()
                .id(reservationRequest.getId())
                .assetCopyId(reservationRequest.getAssetCopy().getId())
                .requesterId(reservationRequest.getRequester().getId())
                .managerId(reservationRequest.getManager() != null ? reservationRequest.getManager().getId() : null)
                .startDateAsked(reservationRequest.getStartDateAsked())
                .returnDateAsked(reservationRequest.getReturnDateAsked())
                .status(ReservationRequestStatus.PENDING)
                .build();
    }

    public ReservationRequestResponseBody toDto(ReservationRequest reservationRequest, ReservationRequestStatus status, String declineReason) {
        return ReservationRequestResponseBody.builder()
                .id(reservationRequest.getId())
                .assetCopyId(reservationRequest.getAssetCopy().getId())
                .requesterId(reservationRequest.getRequester().getId())
                .managerId(reservationRequest.getManager() != null ? reservationRequest.getManager().getId() : null)
                .startDateAsked(reservationRequest.getStartDateAsked())
                .returnDateAsked(reservationRequest.getReturnDateAsked())
                .status(status)
                .declineReason(declineReason)
                .build();
    }
}
