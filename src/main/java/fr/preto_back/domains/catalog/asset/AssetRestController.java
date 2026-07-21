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
@RequestMapping("/api/catalog/assets")
@AllArgsConstructor
public class AssetRestController {
    private final AssetService assetService;

    // TODO : Replace AssetDTO by AssetRequest
    // TODO : Replace return type Asset by AssetDTO and use AssetMapper toDto() method for mapping

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<AssetDTO>> createAsset(@Valid @RequestBody AssetRequest assetRequest) {
        AssetDTO createdAsset = assetService.createAsset(assetRequest);
        ApiResponse<AssetDTO> response = ApiResponse.success(ApiCode.ASSET_SAVE_SUCCESS.name(), ApiCode.ASSET_SAVE_SUCCESS.getMessage(), createdAsset);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AssetDTO>>> getAllAssets() {
        List<AssetDTO> assets = assetService.findAllAssets();

        ApiResponse<List<AssetDTO>> response = ApiResponse.success(ApiCode.ASSETS_FOUND_SUCCESS.name(), ApiCode.ASSETS_FOUND_SUCCESS.getMessage(), assets);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetDTO>> getAssetById(@PathVariable String id) {
        AssetDTO asset = assetService.findAssetById(Integer.parseInt(id.trim()));
        ApiResponse<AssetDTO> response = ApiResponse.success(ApiCode.ASSET_FOUND_SUCCESS.name(),  ApiCode.ASSET_FOUND_SUCCESS.getMessage(), asset);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<AssetDTO>> updateAsset(@PathVariable String id, @Valid @RequestBody AssetRequest aassetRequest) {
        AssetDTO updatedAsset = assetService.updateAsset(Integer.parseInt(id.trim()), aassetRequest);
        ApiResponse<AssetDTO> response = ApiResponse.success(ApiCode.ASSET_UPDATE_SUCCESS.name(), ApiCode.ASSET_UPDATE_SUCCESS.getMessage(), updatedAsset);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<AssetDTO>> deleteAsset(@PathVariable String id) {
        assetService.deleteAsset(Integer.parseInt(id.trim()));
        ApiResponse<AssetDTO> response = ApiResponse.success(ApiCode.ASSET_DELETE_SUCCESS.name(), ApiCode.ASSET_DELETE_SUCCESS.getMessage(), null);

        return ResponseEntity.ok(response);
    }

}
