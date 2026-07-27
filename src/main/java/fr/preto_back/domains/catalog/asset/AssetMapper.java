package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopyMapper;
import fr.preto_back.domains.catalog.category.CategoryResponseBody;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AssetMapper {
    private final AssetCopyMapper assetCopyMapper;

    public AssetResponseBody toDto(Asset asset) {
        return AssetResponseBody.builder()
                .id(asset.getId())
                .title(asset.getTitle())
                .description(asset.getDescription())
                .imageUrl(asset.getImageUrl())
                .category(CategoryResponseBody.builder()
                        .id(asset.getCategory().getId())
                        .name(asset.getCategory().getName())
                        .build())
                .copies(asset.getCopies()
                        .stream()
                        .map(assetCopyMapper::toDto)
                        .toList())
                .build();
    }
}
