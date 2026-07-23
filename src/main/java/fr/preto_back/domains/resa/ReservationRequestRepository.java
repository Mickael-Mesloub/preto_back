package fr.preto_back.domains.resa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Integer> {
    List<ReservationRequest> findReservationRequestByStatusIs(ReservationRequestStatus status);
}
