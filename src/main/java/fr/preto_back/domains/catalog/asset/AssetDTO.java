package fr.preto_back.domains.catalog.asset;
import fr.preto_back.domains.catalog.category.CategoryDTO;
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
public class AssetDTO {
    Integer id;
    String title;
    String description;
    String imageUrl;
    CategoryDTO category;
}
