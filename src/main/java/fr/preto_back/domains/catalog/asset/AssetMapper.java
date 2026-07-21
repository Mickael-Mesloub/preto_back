package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.category.CategoryDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssetMapper {
    public AssetDTO toDto(Asset asset) {
        return AssetDTO.builder()
                .id(asset.getId())
                .title(asset.getTitle())
                .description(asset.getDescription())
                .imageUrl(asset.getImageUrl())
                .category(CategoryDTO.builder()
                        .id(asset.getCategory().getId())
                        .name(asset.getCategory().getName())
                        .build())
                .build();
    }
}
