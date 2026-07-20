package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopyDTO;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRequest;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyService;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
import fr.preto_back.domains.catalog.category.Category;
import fr.preto_back.domains.catalog.category.CategoryRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.preto_back.utils.StringUtils.trimOrNull;

@AllArgsConstructor
@Service
@Slf4j
public class AssetService {
    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final AssetCopyService assetCopyService;

    // TODO : Replace AssetDTO by AssetRequest
    // TODO : Replace return type Asset by AssetDTO and use AssetMapper toDto() method for mapping

    // TODO : Check auth + role
    public Asset createAsset(AssetDTO assetDTO) {

        Category category = categoryRepository.findById(assetDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.CATEGORY_NOT_FOUND, ApiCode.CATEGORY_NOT_FOUND.getMessage()  + " with id " + assetDTO.getCategoryId()));

        Asset newAsset = Asset.builder()
                .title(assetDTO.getTitle().trim())
                .description(trimOrNull(assetDTO.getDescription()))
                .imageUrl(trimOrNull(assetDTO.getImageUrl()))
                .category(category)
                .build();

       Asset savedAsset = assetRepository.save(newAsset);
       AssetCopyRequest assetCopyRequest = AssetCopyRequest.builder()
               .state(AssetCopyState.NEW)
               .build();

       // Create copy for the new asset
        AssetCopyDTO createdCopy = assetCopyService.createAssetCopy(savedAsset.getId(), assetCopyRequest);
        log.info("Asset copy has been created: {}", createdCopy);

        return savedAsset;
    }

    public List<Asset> findAllAssets() {
        return assetRepository.findAll();
    }

    public Asset findAssetById(int assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));
    }

    // TODO : Check auth + role
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

    // TODO : Check auth + role
    public void deleteAsset(int assetId) {
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        // TODO : loop through all asset copies linked to this asset
        // TODO     and delete them + the asset only if no loan is active

        assetRepository.deleteById(assetId);
    }

}
