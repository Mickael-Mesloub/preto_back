package fr.preto_back.domains.catalog.assetcopy;

import fr.preto_back.domains.resa.ReservationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AssetCopyRepository extends JpaRepository<AssetCopy, Integer> {
    List<AssetCopy> findAllByAssetId(Integer assetId);

    // This query returns the first available asset copy, taking into account :
        // copy state => states MAINTENANCE, LOST and RETIRED make a copy unavailable
        // date range (startDateAsked - returnDateAsked) and resa status => we'll also check if no reservation is currently PENDING or APPROVED for a copy of the asset between startDateAsked and returnDateAsked
    @Query("""
                SELECT c FROM AssetCopy c
                WHERE c.asset.id = :assetId
                AND c.state NOT IN :unavailableCopyStates
                AND NOT EXISTS (
                    SELECT r FROM ReservationRequest r
                    WHERE r.assetCopy = c
                    AND r.status IN :activeReservationStatuses
                    AND r.startDateAsked < :returnDate
                    AND r.returnDateAsked > :startDate
                )
            ORDER BY c.id ASC
            """)
    List<AssetCopy> findAvailableCopies(
            Integer assetId,
            LocalDateTime startDate,
            LocalDateTime returnDate,
            List<AssetCopyState> unavailableCopyStates,
            List<ReservationRequestStatus> activeReservationStatuses
    );
}
