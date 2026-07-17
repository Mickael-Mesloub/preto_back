package fr.preto_back.domains.catalog.asset;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/catalog")
@AllArgsConstructor
public class AssetRestController {
    private final AssetService assetService;

    @PostMapping("/assets/new")
    public ResponseEntity<ApiResponse<Asset>> createAsset(@Valid @RequestBody AssetDTO aassetDTO) {
        Asset createdAsset = assetService.createAsset(aassetDTO);
        ApiResponse<Asset> response = ApiResponse.success(ApiCode.ASSET_SAVE_SUCCESS.name(), ApiCode.ASSET_SAVE_SUCCESS.getMessage(), createdAsset);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/assets/all")
    public ResponseEntity<ApiResponse<List<Asset>>> getAllAssets() {
        List<Asset> assets = assetService.findAllAssets();
        ApiResponse<List<Asset>> response = ApiResponse.success(ApiCode.ASSETS_FOUND_SUCCESS.name(),  ApiCode.ASSETS_FOUND_SUCCESS.getMessage(), assets);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/assets/{id}")
    public ResponseEntity<ApiResponse<Asset>> getAssetById(@PathVariable String id) {
        Asset asset = assetService.findAssetById(Integer.parseInt(id.trim()));
        ApiResponse<Asset> response = ApiResponse.success(ApiCode.ASSET_FOUND_SUCCESS.name(),  ApiCode.ASSET_FOUND_SUCCESS.getMessage(), asset);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/assets/{id}/update")
    public ResponseEntity<ApiResponse<Asset>> updateAsset(@PathVariable String id, @Valid @RequestBody AssetDTO aassetDTO) {
        Asset updatedAsset = assetService.updateAsset(Integer.parseInt(id.trim()), aassetDTO);
        ApiResponse<Asset> response = ApiResponse.success(ApiCode.ASSET_UPDATE_SUCCESS.name(), ApiCode.ASSET_UPDATE_SUCCESS.getMessage(), updatedAsset);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/assets/{id}/delete")
    public ResponseEntity<ApiResponse<Asset>> deleteAsset(@PathVariable String id) {
        assetService.deleteAsset(Integer.parseInt(id.trim()));
        ApiResponse<Asset> response = ApiResponse.success(ApiCode.ASSET_DELETE_SUCCESS.name(), ApiCode.ASSET_DELETE_SUCCESS.getMessage(), null);

        return ResponseEntity.ok(response);
    }

}
