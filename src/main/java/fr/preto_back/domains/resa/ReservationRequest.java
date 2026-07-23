package fr.preto_back.domains.resa;

import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "RESERVATION_REQUEST")
public class ReservationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "COPY_ID", nullable = false, referencedColumnName = "id")
    private AssetCopy assetCopy;

    @Column(name = "START_DATE_ASKED", nullable = false)
    private LocalDateTime startDateAsked;

    @Column(name = "RETURN_DATE_ASKED")
    private LocalDateTime returnDateAsked;

    // User who sends a reservation request
    @ManyToOne
    @JoinColumn(name = "REQUESTER_ID", referencedColumnName = "id")
    private User requester;

    // User (with MANAGER role) who treats the reservation requests
    @ManyToOne
    @JoinColumn(name = "MANAGER_ID", referencedColumnName = "id", nullable = true)
    private User manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private @Builder.Default ReservationRequestStatus status = ReservationRequestStatus.PENDING;
}
