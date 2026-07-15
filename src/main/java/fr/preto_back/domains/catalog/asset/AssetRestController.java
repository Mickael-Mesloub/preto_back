package fr.preto_back.domains.catalog.asset;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.coyote.http11.Constants.a;

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
}
