package fr.preto_back.domains.catalog.assetcopy;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/catalog/assets")
@AllArgsConstructor
public class AssetCopyRestController {
    private final AssetCopyService assetCopyService;

    @PostMapping("/{assetId}/new-copy")
    public ResponseEntity<ApiResponse<AssetCopyDTO>> createAssetCopy(
            @PathVariable String assetId,
            @Valid @RequestBody AssetCopyRequest assetCopyRequest
    ) {
        AssetCopyDTO createdAssetCopy = assetCopyService.createAssetCopy(Integer.parseInt(assetId.trim()), assetCopyRequest);
        ApiResponse<AssetCopyDTO> response = ApiResponse.success(ApiCode.ASSET_COPY_SAVE_SUCCESS.name(), ApiCode.ASSET_COPY_SAVE_SUCCESS.getMessage(), createdAssetCopy);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/copies/{id}")
    public ResponseEntity<ApiResponse<AssetCopyDTO>> getAssetCopyById(@PathVariable String id) {
        AssetCopyDTO assetCopy = assetCopyService.findById(Integer.parseInt(id.trim()));
        ApiResponse<AssetCopyDTO> response = ApiResponse.success(ApiCode.ASSET_COPY_FOUND_SUCCESS.name(), ApiCode.ASSET_COPY_FOUND_SUCCESS.getMessage(), assetCopy);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/copies/all")
    public ResponseEntity<ApiResponse<List<AssetCopyDTO>>> getAllAssetCopies() {
        List<AssetCopyDTO> assetCopies = assetCopyService.findAllAssetCopies();
        ApiResponse<List<AssetCopyDTO>> response = ApiResponse.success(ApiCode.ASSET_COPIES_FOUND_SUCCESS.name(), ApiCode.ASSET_COPIES_FOUND_SUCCESS.getMessage(), assetCopies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{assetId}/copies")
    public ResponseEntity<ApiResponse<List<AssetCopyDTO>>> getCopiesByAssetId(@PathVariable String assetId) {
        List<AssetCopyDTO> assetCopies = assetCopyService.findCopiesByAssetId(Integer.parseInt(assetId.trim()));
        ApiResponse<List<AssetCopyDTO>> response = ApiResponse.success(ApiCode.ASSET_COPIES_FOUND_SUCCESS.name(), ApiCode.ASSET_COPIES_FOUND_SUCCESS.getMessage(), assetCopies);

        return ResponseEntity.ok(response);
    }

}
