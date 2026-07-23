package fr.preto_back.domains.resa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Integer> {
}
