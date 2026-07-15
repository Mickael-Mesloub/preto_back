package fr.preto_back.domains.catalog.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

@Entity
@Table(name = "ASSET")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "{VALIDATION_ASSET_TITLE_REQUIRED}")
    @Size(min = 3, max = 255, message = "{VALIDATION_ASSET_TITLE_SIZE}")
    private String title;

    @Column(name = "description", length = 1000)
    @Size(max = 1000, message = "{VALIDATION_ASSET_DESCRIPTION_SIZE}")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    // TODO: add category
}
