package fr.preto_back.domains.catalog.asset;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@Slf4j
@SpringBootTest
public class TestAssetRepository {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EntityManager em;

    @Test
    void test_create_OK() {
        Asset newAsset = Asset.builder()
                .title("Ordinateur portable")
                .description("15 pouces")
                .build();

        assetRepository.save(newAsset);
        assetRepository.flush();
        em.clear();

        Optional<Asset> optAsset = assetRepository.findById(newAsset.getId());

        Assertions.assertThat(optAsset.isPresent()).isTrue();
        Assertions.assertThat(optAsset.get().getId()).isNotNull();
        Assertions.assertThat(optAsset.get().getTitle()).isEqualTo(newAsset.getTitle());
    }

    @Test
    void test_create_KO_title_missing() {
        Asset newAsset = Asset.builder()
                .description("15 pouces")
                .build();

        Assertions.assertThatThrownBy(() -> {
            assetRepository.save(newAsset);
            assetRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    // TODO add tests with missing data/fields
}
