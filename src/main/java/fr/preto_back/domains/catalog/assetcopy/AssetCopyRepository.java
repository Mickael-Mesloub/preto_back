package fr.preto_back.domains.catalog.assetcopy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetCopyRepository extends JpaRepository<AssetCopy, Integer> {
    List<AssetCopy> findAllByAssetId(Integer assetId);
}
