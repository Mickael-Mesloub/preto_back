package fr.preto_back.domains.resa;

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
    // TODO: validation
        // startDate @FutureOfPresent
        // returnDate @Future
    LocalDateTime startDate;
    LocalDateTime returnDate;
}
