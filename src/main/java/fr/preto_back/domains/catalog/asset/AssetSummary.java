package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
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
// This class regroups essential information about an asset that front may need to access and display
// on screens (for reservation requests, loans...)
public class AssetSummary {
    private String assetTitle;
    private String assetDescription;
    private AssetCopyState state;
}
