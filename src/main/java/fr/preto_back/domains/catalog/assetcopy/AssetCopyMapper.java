package fr.preto_back.domains.catalog.assetcopy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssetCopyMapper {
    // Maps entity to dto and add isAvailable
    public AssetCopyDTO toDto(
            AssetCopy assetCopy
            // boolean isAvailable
    ) {
        return AssetCopyDTO.builder()
                .id(assetCopy.getId())
                .asset(assetCopy.getAsset())
                .state(assetCopy.getState())
                // .isAvailable(isAvailable)
                .build();
    }
}
