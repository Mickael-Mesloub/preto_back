package fr.preto_back.domains.resa;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ProcessReservationRequestBody {
    @NotNull(message = "{VALIDATION_RESERVATION_DECISION_REQUIRED}")
    ReservationRequestDecision decision;

    String declineReason;
}
