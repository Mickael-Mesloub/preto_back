package fr.preto_back.domains.catalog.asset;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestAssetService {
    @Autowired
    private AssetService assetService;

    @Test
    public void testCreateAsset_OK() {
        // Instantiate valid assetRequest
        AssetRequest assetRequest = AssetRequest.builder()
                .title("Ordinateur portable")
                .description("15 pouces")
                .imageUrl(null)
                .categoryId(1)
                .build();

        // Create AssetDTO using createAsset method from service
        AssetDTO createdAsset = assetService.createAsset(assetRequest);
        log.info("Asset has been created: {}", createdAsset);

        // Test that the AssetDTO has been created with testing its id
        Assertions.assertThat(createdAsset.getId()).isNotNull();

        // Test that a copy has been created for this asset
        Assertions.assertThat(createdAsset.getCopies()).isNotEmpty();
    }

    @Test
    public void testCreateAsset_KO_CategoryDoesNotExist() {
        // Instantiate invalid assetRequest -> with category id that does not exist
        AssetRequest assetRequest = AssetRequest.builder()
                .title("Ordinateur portable")
                .description("15 pouces")
                .imageUrl(null)
                .categoryId(999)
                .build();

        // Test that createAsset method from service throws a vResourceNotFoundException
        Assertions.assertThatThrownBy(() -> assetService.createAsset(assetRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ApiCode.CATEGORY_NOT_FOUND.getMessage());
    }

}
