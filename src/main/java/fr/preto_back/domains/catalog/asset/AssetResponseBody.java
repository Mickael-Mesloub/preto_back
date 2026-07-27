package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopyResponseBody;
import fr.preto_back.domains.catalog.category.CategoryResponseBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class AssetResponseBody {
    Integer id;
    String title;
    String description;
    String imageUrl;
    CategoryResponseBody category;
    List<AssetCopyResponseBody> copies;
}
