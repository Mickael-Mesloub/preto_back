package fr.preto_back.domains.catalog.asset;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssetService {
    private final AssetRepository assetRepository;

    public Asset createAsset(AssetDTO assetDTO) {
        Asset newAsset = Asset.builder()
                .title(assetDTO.getTitle())
                .description(assetDTO.getDescription())
                .imageUrl(assetDTO.getImageUrl())
                .build();

       return assetRepository.save(newAsset);
    }

    /*public ResponseEntity<ApiResponse<List<Asset>>> findAll() {
        try {

        } catch (

        )
    }*/

}
