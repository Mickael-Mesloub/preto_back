package fr.preto_back.domains.catalog.assetcopy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AssetCopyHelper {
    // Check if a copy is in a good-enough physical state for reservation.
    // A copy cannot be available if under maintenance, retired or lost
    public boolean checkCopyAvailableState(AssetCopy assetCopy) {
        return assetCopy.getState() != AssetCopyState.MAINTENANCE
                && assetCopy.getState() != AssetCopyState.RETIRED
                && assetCopy.getState() != AssetCopyState.LOST;
    }

    // Return the first available copy depending on physical state and resa/loan dates asked
    // TODO: check date range availability
    public Optional<AssetCopy> getFirstAvailableAssetCopy(List<AssetCopy> copies) {

        return copies.stream()
                .filter(this::checkCopyAvailableState)
                .findFirst();
    }
}
