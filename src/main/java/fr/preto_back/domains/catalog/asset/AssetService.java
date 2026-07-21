package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyMapper;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
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
    private final AssetCopyRepository assetCopyRepository;
    private final CategoryRepository categoryRepository;
    private final AssetCopyService assetCopyService;
    private final AssetMapper assetMapper;
    private final AssetCopyMapper assetCopyMapper;

    // TODO : Check auth + role
    public AssetDTO createAsset(AssetRequest assetRequest) {

        Category category = categoryRepository.findById(assetRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.CATEGORY_NOT_FOUND, ApiCode.CATEGORY_NOT_FOUND.getMessage() + " with id " + assetRequest.getCategoryId()));

        Asset savedAsset = assetRepository.save(Asset.builder()
                .title(trimOrNull(assetRequest.getTitle()))
                .description(trimOrNull(assetRequest.getDescription()))
                .imageUrl(trimOrNull(assetRequest.getImageUrl()))
                .category(category)
                .build());

        AssetCopy copy = AssetCopy.builder()
                .asset(savedAsset)
                .state(AssetCopyState.NEW)
                .build();

        AssetCopy savedCopy = assetCopyRepository.save(copy);

        savedAsset.addCopy(savedCopy);

        return assetMapper.toDto(savedAsset);
    }

    public List<AssetDTO> findAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assets.stream()
                .map(assetMapper::toDto)
                .toList();
    }

    public AssetDTO findAssetById(int assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        return assetMapper.toDto(asset);
    }

    // TODO : Check auth + role
    public AssetDTO updateAsset(int assetId, AssetRequest assetRequest) {
        // Check if asset with assetid provided exists. If not, throw custom Not found exception
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        // Check if category provided exists. If not, throw custom Not found exception
        Category existingCategory = categoryRepository.findById(assetRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.CATEGORY_NOT_FOUND, ApiCode.CATEGORY_NOT_FOUND.getMessage() + " with id " + assetRequest.getCategoryId()));

        // Update existing asset with new data
        existingAsset.setTitle(trimOrNull(assetRequest.getTitle()));
        existingAsset.setDescription(trimOrNull(assetRequest.getDescription()));
        existingAsset.setImageUrl(trimOrNull(assetRequest.getImageUrl()));
        existingAsset.setCategory(existingCategory);

        // Save changes in base
        Asset updatedAsset = assetRepository.save(existingAsset);

        // Map entity to DTO and return DTO to controller, which then returns it to client
        return assetMapper.toDto(updatedAsset);
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
