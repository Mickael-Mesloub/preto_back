package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.category.Category;
import fr.preto_back.domains.catalog.category.CategoryRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager em;

    private Category category;

    @BeforeEach
    void init() {
        category = categoryRepository.findById(1).orElse(null);
    }

    @Test
    void test_create_OK() {
        Asset newAsset = Asset.builder()
                .title("Ordinateur portable")
                .description("15 pouces")
                .category(category)
                .build();

        assetRepository.save(newAsset);
        assetRepository.flush();
        em.clear();

        Optional<Asset> optAsset = assetRepository.findById(newAsset.getId());

        log.info(optAsset.toString());

        Assertions.assertThat(optAsset.isPresent()).isTrue();
        Assertions.assertThat(optAsset.get().getId()).isNotNull();
        Assertions.assertThat(optAsset.get().getTitle()).isEqualTo(newAsset.getTitle());
    }

    @Test
    void test_create_KO_title_missing() {
        Asset newAsset = Asset.builder()
                .description("15 pouces")
                .category(category)
                .build();

        Assertions.assertThatThrownBy(() -> {
            assetRepository.save(newAsset);
            assetRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
