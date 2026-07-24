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
    ASSET_COPY_FOUND_SUCCESS("Asset copy found"),
    ASSET_COPIES_FOUND_SUCCESS("Asset copies found"),
    ASSET_COPY_NO_AVAILABLE_COPY("No available copy was found"),

    // Category
    CATEGORY_NOT_FOUND("Category not found"),

    // Reservation Request
    RESA_SAVE_SUCCESS("New reservation request sent successfully"),
    RESAS_FOUND_SUCCESS("Reservation requests found"),
    RESA_NOT_FOUND("Reservation request not found"),
    RESA_NOT_PENDING("Only PENDING reservation requests can be processed"),
    RESA_DECLINE_MISSING_REASON("A reason must be provided if declining a reservation request"),
    RESA_INVALID_DECISION("Decision must be either APPROVED or DECLINED"),

    // User
    USER_NOT_FOUND("User not found");


    private final String message;

    ApiCode(String message) { this.message = message; }
}
