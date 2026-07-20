package fr.preto_back.shared.api_response;

import lombok.Getter;

@Getter
public enum ApiCode {
    // Common
    TEST("Test response message"),
    INTERNAL_SERVER_ERROR("An unexpected error occurred"),
    VALIDATION_ERROR("Validation failed"),
    INVALID_REQUEST_BODY("Request body is missing or invalid"),

    // Asset
    ASSET_NULL("No asset provided"),
    ASSET_SAVE_SUCCESS("Asset created successfully"),
    ASSET_SAVE_FAILED("Asset creation failed"),
    ASSET_NOT_FOUND("Asset not found"),
    ASSET_UPDATE_SUCCESS("Asset updated successfully"),
    ASSET_DELETE_SUCCESS("Asset deleted successfully"),
    ASSETS_FOUND_SUCCESS("Assets found"),
    ASSET_FOUND_SUCCESS("Asset found"),

    // AssetCopy
    ASSET_COPY_NOT_FOUND("Asset copy not found"),
    ASSET_COPY_SAVE_SUCCESS("Asset copy created successfully"),

    // Category
    CATEGORY_NOT_FOUND("Category not found");

    private final String message;

    ApiCode(String message) { this.message = message; }
}
