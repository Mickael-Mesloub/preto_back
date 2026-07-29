package fr.preto_back.domains.catalog.asset;

import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.category.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    private Integer id;

    @Column(name = "TITLE", nullable = false, length = 60)
    private String title;

    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    @Column(name = "IMAGE_URL", length = 255)
    private String imageUrl;

    @JoinColumn(name = "CATEGORY_ID")
    @ManyToOne(optional = false)
    private Category category;

    // No cascade delete and no orphan removal
    // because we want to keep an history of all the copies that have been loaned
    @OneToMany(mappedBy = "asset")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private @Builder.Default List<AssetCopy> copies = new ArrayList<>();

    // Method to add copy to the list of copies
    public void addCopy(AssetCopy copy) {
        copies.add(copy);
        copy.setAsset(this);
    }
}
