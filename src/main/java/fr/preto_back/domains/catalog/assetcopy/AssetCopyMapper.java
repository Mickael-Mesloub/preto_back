package fr.preto_back.domains.catalog.assetcopy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssetCopyMapper {
    private final AssetCopyHelper assetCopyHelper;
    // Maps entity to dto and calculate isAvailable depending on copy physical state
    public AssetCopyResponseBody toDto(AssetCopy assetCopy) {
        return AssetCopyResponseBody.builder()
                .id(assetCopy.getId())
                .assetId(assetCopy.getAsset().getId())
                .state(assetCopy.getState())
                .isAvailable(assetCopyHelper.checkCopyAvailableState(assetCopy))
                .build();
    }
}
