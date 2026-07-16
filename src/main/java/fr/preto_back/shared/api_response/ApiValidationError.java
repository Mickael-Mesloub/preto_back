package fr.preto_back.shared.api_response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiValidationError {
    private String field;
    private String validationErrorMessageKey;
}
