package fr.preto_back.shared.api_response;

import lombok.Getter;

@Getter
public enum ApiCode {
    // Common
    INTERNAL_SERVER_ERROR("An unexpected error occurred"),
    VALIDATION_ERROR("Validation failed"),
    INVALID_REQUEST_BODY("Request body is missing or invalid"),
    ROUTE_NOT_FOUND("Route not found"),

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
    RESA_DECLINED_MISSING_REASON("A reason must be provided if declining a reservation request"),
    RESA_INVALID_DECISION("Decision must be either APPROVED or DECLINED"),
    RESA_PROCESS_SUCCESS("Reservation request processed successfully"),
    RESA_RETURN_DATE_BEFORE_OR_EQUALS_START_DATE("Return date cannot be before or equal to start date"),
    RESA_NOT_APPROVED("Reservation request must be approved before processing loan"),
    // User
    USER_NOT_FOUND("User not found"),

    // Loan
    LOAN_FOUND_SUCCESS("Loan found"),
    LOANS_FOUND_SUCCESS("Loans found"),
    LOAN_NOT_FOUND("Loan not found"),
    LOAN_NOT_ACTIVE("Only ACTIVE loans can be returned"),
    LOAN_NOT_PENDING_CHECKOUT("Only PENDING_CHECKOUT loans can be checked out"),
    LOAN_CHECKOUT_SUCCESS("Loan checked out successfully"),
    LOAN_RETURN_SUCCESS("Loan returned successfully");

    private final String message;

    ApiCode(String message) { this.message = message; }
}
