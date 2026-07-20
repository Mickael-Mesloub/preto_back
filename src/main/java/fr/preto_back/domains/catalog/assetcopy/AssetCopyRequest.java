package fr.preto_back.domains.catalog.assetcopy;

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
public class AssetCopyRequest {
    AssetCopyState state;
    // TODO : uncomment isAvailable when resa/loan ready
    // boolean isAvailable;
}
