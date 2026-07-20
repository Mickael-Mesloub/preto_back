package fr.preto_back.domains.catalog.assetcopy;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AssetCopyService {
    private final AssetCopyRepository assetCopyRepository;
    private final AssetRepository assetRepository;
    AssetCopyMapper mapper;

    public List<AssetCopyDTO> findAllAssetCopies() {
        List<AssetCopy> copies = assetCopyRepository.findAll();

        return copies.stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    public AssetCopyDTO findById(Integer id) {
        AssetCopy assetCopy = assetCopyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_COPY_NOT_FOUND, ApiCode.ASSET_COPY_NOT_FOUND.getMessage() + " with id " + id));

        return mapper.toDto(assetCopy);
    }

    public List<AssetCopyDTO> findAllAssetCopiesByAssetId(Integer assetId) {
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        List<AssetCopy> assetCopies = assetCopyRepository.findAllByAssetId(existingAsset.getId());

        return assetCopies.stream()
                .map(c -> mapper.toDto(c))
                .toList();
    }

    public AssetCopyDTO createAssetCopy(Integer assetId, AssetCopyRequest assetCopyRequest) {
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        AssetCopy newAssetCopy = assetCopyRepository.save(AssetCopy.builder()
                .asset(existingAsset)
                .state(assetCopyRequest.getState())
                .build());

        return mapper.toDto(newAssetCopy);
    }


}
