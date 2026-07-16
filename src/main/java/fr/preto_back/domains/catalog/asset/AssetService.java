package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.category.Category;
import fr.preto_back.domains.catalog.category.CategoryRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;

    public Asset createAsset(AssetDTO assetDTO) {

        Category category = categoryRepository.findById(assetDTO.getCategoryId())
                .orElseThrow(() -> new ApiException(ApiCode.CATEGORY_NOT_FOUND, ApiCode.CATEGORY_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        Asset newAsset = Asset.builder()
                .title(assetDTO.getTitle())
                .description(assetDTO.getDescription())
                .imageUrl(assetDTO.getImageUrl())
                .category(category)
                .build();

       return assetRepository.save(newAsset);
    }

    /*public ResponseEntity<ApiResponse<List<Asset>>> findAll() {
        try {

        } catch (

        )
    }*/

}
