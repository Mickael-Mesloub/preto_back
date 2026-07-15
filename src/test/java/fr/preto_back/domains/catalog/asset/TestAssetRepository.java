package fr.preto_back.domains.catalog.asset;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@Slf4j
@DataJpaTest

public class TestAssetRepository {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EntityManager em;

    @Test
    void test_create() {
        Asset newAsset = Asset.builder()
                .title("Ordinateur portable")
                .description("15 pouces")
                .build();

        assetRepository.save(newAsset);
        assetRepository.flush();
        em.clear();

        Optional<Asset> optAsset = assetRepository.findById(newAsset.getId());
        log.info(optAsset.get().toString());
        Assertions.assertThat(optAsset.isPresent());
        Assertions.assertThat(optAsset.get().getId()).isEqualTo(1);
        Assertions.assertThat(optAsset.get().getTitle()).isEqualTo(newAsset.getTitle());
    }
}
