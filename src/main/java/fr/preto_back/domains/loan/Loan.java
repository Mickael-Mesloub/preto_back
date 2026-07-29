package fr.preto_back.domains.loan;

import fr.preto_back.domains.resa.ReservationRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "LOAN")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // The actual date and time when the asset was checked out by the borrower
    @Column(name = "ACTUAL_CHECKOUT_DATE")
    private LocalDateTime actualCheckoutDate;

    // The actual date and time when the asset was returned by the borrower
    @Column(name = "ACTUAL_RETURN_DATE")
    private LocalDateTime actualReturnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private @Builder.Default LoanStatus status = LoanStatus.PENDING_CHECKOUT;

    @OneToOne
    @JoinColumn(name = "RESA_ID", nullable = false)
    private ReservationRequest reservationRequest;
}
