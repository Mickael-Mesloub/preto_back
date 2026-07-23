package fr.preto_back.domains.catalog.asset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class AssetRequestBody {
    @NotBlank(message = "{VALIDATION_ASSET_TITLE_REQUIRED}")
    @Size(min = 3, max = 60, message = "{VALIDATION_ASSET_TITLE_SIZE}")
    String title;

    @Size(max = 1000, message = "{VALIDATION_ASSET_DESCRIPTION_SIZE}")
    String description;

    @Size(min = 3, max = 255, message = "{VALIDATION_ASSET_IMAGE_URL_SIZE}")
    String imageUrl;

    @NotNull(message = "{VALIDATION_ASSET_CATEGORY_REQUIRED}")
    Integer categoryId;
}
