package fr.preto_back.shared.api_response;

import lombok.Getter;

@Getter
public enum ApiCode {
    TEST("Test response message"),
    INTERNAL_SERVER_ERROR("An unexpected error occurred"),
    VALIDATION_ERROR("Validation failed"),
    ASSET_NULL("No asset provided"),
    ASSET_SAVE_SUCCESS("Asset saved successfully"),
    ASSET_SAVE_FAILED("Asset save failed");

    private final String message;

    ApiCode(String message) { this.message = message; }
}
