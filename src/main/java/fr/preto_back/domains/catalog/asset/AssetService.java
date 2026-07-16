package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.category.Category;
import fr.preto_back.domains.catalog.category.CategoryRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;

    public Asset createAsset(AssetDTO assetDTO) {

        Category category = categoryRepository.findById(assetDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.CATEGORY_NOT_FOUND, ApiCode.CATEGORY_NOT_FOUND.getMessage()  + " with id " + assetDTO.getCategoryId()));

        Asset newAsset = Asset.builder()
                .title(assetDTO.getTitle().trim())
                .description(assetDTO.getDescription().trim())
                .imageUrl(assetDTO.getImageUrl().trim())
                .category(category)
                .build();

       return assetRepository.save(newAsset);
    }

    public Asset updateAsset(int assetId, AssetDTO assetDTO) {
        // Check if asset with assetid provided exists. If not, throw custom Not found exception
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        // Check if category provided exists. If not, throw custom Not found exception
        Category existingCategory = categoryRepository.findById(assetDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.CATEGORY_NOT_FOUND, ApiCode.CATEGORY_NOT_FOUND.getMessage()  + " with id " + assetDTO.getCategoryId()));

        // Update existing asset with new data
        existingAsset.setTitle(assetDTO.getTitle());
        existingAsset.setDescription(assetDTO.getDescription());
        existingAsset.setImageUrl(assetDTO.getImageUrl());
        existingAsset.setCategory(existingCategory);

        return assetRepository.save(existingAsset);
    }

    public void deleteAsset(int assetId) {
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        assetRepository.deleteById(assetId);
    }

    /*public ResponseEntity<ApiResponse<List<Asset>>> findAll() {
        try {

        } catch (

        )
    }*/

}
