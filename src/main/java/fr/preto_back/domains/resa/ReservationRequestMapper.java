package fr.preto_back.domains.resa;

import fr.preto_back.domains.catalog.asset.AssetSummary;
import fr.preto_back.domains.user.UserSummary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReservationRequestMapper {
    public ReservationRequestResponseBody toDto(
            ReservationRequest reservationRequest,
            AssetSummary assetSummary,
            UserSummary requesterSummary
    ) {
        return ReservationRequestResponseBody.builder()
                .id(reservationRequest.getId())
                .asset(assetSummary)
                .requester(requesterSummary)
                .manager(null)
                .startDateAsked(reservationRequest.getStartDateAsked())
                .returnDateAsked(reservationRequest.getReturnDateAsked())
                .status(ReservationRequestStatus.PENDING)
                .build();
    }

    public ReservationRequestResponseBody toDto(
            ReservationRequest reservationRequest,
            AssetSummary assetSummary,
            UserSummary requesterSummary,
            UserSummary managerSummary,
            ReservationRequestStatus status,
            String declineReason
    ) {
        return ReservationRequestResponseBody.builder()
                .id(reservationRequest.getId())
                .asset(assetSummary)
                .requester(requesterSummary)
                .manager(managerSummary)
                .startDateAsked(reservationRequest.getStartDateAsked())
                .returnDateAsked(reservationRequest.getReturnDateAsked())
                .status(status)
                .declineReason(declineReason)
                .build();
    }
}
