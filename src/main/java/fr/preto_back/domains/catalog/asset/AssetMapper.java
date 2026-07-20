package fr.preto_back.domains.catalog.asset;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssetMapper {
    public AssetDTO toDto(Asset asset) {
        return AssetDTO.builder()
                .title(asset.getTitle())
                .description(asset.getDescription())
                .imageUrl(asset.getImageUrl())
                .categoryId(asset.getCategory().getId())
                .build();
    }
}
