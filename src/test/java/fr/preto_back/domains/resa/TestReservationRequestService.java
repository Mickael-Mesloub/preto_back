package fr.preto_back.domains.resa;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
public class TestReservationRequestService {
    @Autowired
    private ReservationRequestService reservationRequestService;

    @Test
    public void testNewReservationRequest_OK() {
        Integer userId = 3;
        Integer assetId = 8;

        ReservationRequestBody reservationRequestBody = ReservationRequestBody.builder()
                .startDate(LocalDateTime.of(2026, 8, 1, 12, 5 ))
                .returnDate(LocalDateTime.of(2026, 8,1, 15, 30 ))
                .build();

        log.info("New reservation request: {}", reservationRequestBody);

        ReservationRequestDTO reservationRequestDto = reservationRequestService.newReservationRequest(userId, assetId, reservationRequestBody);
        log.info("New reservation request dto: {}", reservationRequestDto);

        Assertions.assertThat(reservationRequestDto).isNotNull();
    }

    @Test
    public void testFindReservationRequestsByStatus_PENDING_OK() {
        Integer userId = 3;
        Integer assetId = 8;

        ReservationRequestBody reservationRequestBody = ReservationRequestBody.builder()
                .startDate(LocalDateTime.of(2026, 8, 1, 12, 5 ))
                .returnDate(LocalDateTime.of(2026, 8,1, 15, 30 ))
                .build();

        // Create new request (by default, status = PENDING)
        ReservationRequestDTO reservationRequestDto = reservationRequestService.newReservationRequest(userId, assetId, reservationRequestBody);

        // Retrieve all requests with status == PENDING
        List<ReservationRequestDTO> pendingRequests = reservationRequestService.findReservationRequestsByStatus(ReservationRequestStatus.PENDING);

        // The list should not be empty
        Assertions.assertThat(pendingRequests).isNotEmpty();
    }
}
