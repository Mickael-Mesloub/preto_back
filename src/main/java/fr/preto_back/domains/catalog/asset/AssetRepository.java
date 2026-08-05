package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
import fr.preto_back.domains.resa.ReservationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Integer> {
    @Query("""
    SELECT DISTINCT a FROM Asset a
    WHERE EXISTS (
        SELECT c FROM AssetCopy c
        WHERE c.asset = a
        AND c.state NOT IN :unavailableStates
        AND NOT EXISTS (
            SELECT r FROM ReservationRequest r
            WHERE r.assetCopy = c
            AND r.status IN :activeStatuses
            AND r.startDateAsked < :returnDate
            AND r.returnDateAsked > :startDate
        )
    )
    """)
    List<Asset> findAssetsWithAvailableCopy(
            LocalDateTime startDate,
            LocalDateTime returnDate,
            List<AssetCopyState> unavailableStates,
            List<ReservationRequestStatus> activeStatuses
    );
}
