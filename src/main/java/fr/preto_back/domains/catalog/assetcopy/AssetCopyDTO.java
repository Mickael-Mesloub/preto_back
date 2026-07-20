package fr.preto_back.domains.catalog.assetcopy;

import fr.preto_back.domains.catalog.asset.Asset;
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
public class AssetCopyDTO {
    Integer id;
    Asset asset;
    AssetCopyState state;
    // TODO : uncomment isAvailable when resa/loan ready
    // boolean isAvailable;
}
