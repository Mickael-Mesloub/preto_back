package fr.preto_back.domains.resa;

import fr.preto_back.domains.catalog.asset.AssetSummary;
import fr.preto_back.domains.user.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ReservationRequestResponseBody {
    Integer id;
    AssetSummary asset;
    UserSummary requester;
    LocalDateTime startDateAsked;
    LocalDateTime returnDateAsked;
    ReservationRequestStatus status;
    String declineReason;
    UserSummary manager;
}
