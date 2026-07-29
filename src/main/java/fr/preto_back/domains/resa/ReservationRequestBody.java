package fr.preto_back.domains.resa;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
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
public class ReservationRequestBody {
    // startDate must be in the future
    @Future(message = "{VALIDATION_RESA_START_DATE_MUST_BE_IN_FUTURE}")
    @NotNull(message = "{VALIDATION_RESA_START_DATE_REQUIRED}")
    LocalDateTime startDate;

    // returnDate must be in the future
    @Future(message = "{VALIDATION_RESA_RETURN_DATE_MUST_BE_IN_FUTURE}")
    @NotNull(message = "{VALIDATION_RESA_RETURN_DATE_REQUIRED}")
    LocalDateTime returnDate;
}
