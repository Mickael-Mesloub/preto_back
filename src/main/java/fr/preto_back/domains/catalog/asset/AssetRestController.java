package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.resa.ReservationRequestBody;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@RestController
@CrossOrigin
@RequestMapping("/api/catalog/assets")
@AllArgsConstructor
public class AssetRestController {
    private final AssetService assetService;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<AssetResponseBody>> createAsset(@Valid @RequestBody AssetRequestBody assetRequestBody) {
        AssetResponseBody createdAsset = assetService.createAsset(assetRequestBody);
        ApiResponse<AssetResponseBody> response = ApiResponse.success(ApiCode.ASSET_SAVE_SUCCESS.name(), ApiCode.ASSET_SAVE_SUCCESS.getMessage(), createdAsset);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AssetResponseBody>>> getAllAssets() {
        List<AssetResponseBody> assets = assetService.findAllAssets();

        ApiResponse<List<AssetResponseBody>> response = ApiResponse.success(ApiCode.ASSETS_FOUND_SUCCESS.name(), ApiCode.ASSETS_FOUND_SUCCESS.getMessage(), assets);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssetResponseBody>> getAssetById(@PathVariable String id) {
        AssetResponseBody asset = assetService.findAssetById(Integer.parseInt(id.trim()));
        ApiResponse<AssetResponseBody> response = ApiResponse.success(ApiCode.ASSET_FOUND_SUCCESS.name(),  ApiCode.ASSET_FOUND_SUCCESS.getMessage(), asset);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/check-availability")
    public ResponseEntity<ApiResponse<CheckAvailabilityResponseBody>> checkAvailability(
            @PathVariable String id,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "returnDate") @DateTimeFormat(iso = DATE_TIME) LocalDateTime returnDate
    ) {
        CheckAvailabilityResponseBody checkAvailability = assetService.checkAvailability(Integer.parseInt(id.trim()), ReservationRequestBody.builder()
                .startDate(startDate)
                .returnDate(returnDate)
                .build()
        );

        // Send a different response and message depending on availability
        ApiResponse<CheckAvailabilityResponseBody> response =
                checkAvailability.isAvailable()
                ? ApiResponse.success(ApiCode.ASSET_COPY_AVAILABLE.name(), ApiCode.ASSET_COPY_AVAILABLE.getMessage(), checkAvailability)
                : ApiResponse.success(ApiCode.ASSET_NO_COPY_AVAILABLE.name(), ApiCode.ASSET_NO_COPY_AVAILABLE.getMessage(), checkAvailability);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<AssetResponseBody>> updateAsset(@PathVariable String id, @Valid @RequestBody AssetRequestBody aassetRequestBody) {
        AssetResponseBody updatedAsset = assetService.updateAsset(Integer.parseInt(id.trim()), aassetRequestBody);
        ApiResponse<AssetResponseBody> response = ApiResponse.success(ApiCode.ASSET_UPDATE_SUCCESS.name(), ApiCode.ASSET_UPDATE_SUCCESS.getMessage(), updatedAsset);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<AssetResponseBody>> deleteAsset(@PathVariable String id) {
        assetService.deleteAssetById(Integer.parseInt(id.trim()));
        ApiResponse<AssetResponseBody> response = ApiResponse.success(ApiCode.ASSET_DELETE_SUCCESS.name(), ApiCode.ASSET_DELETE_SUCCESS.getMessage(), null);

        return ResponseEntity.ok(response);
    }

}
