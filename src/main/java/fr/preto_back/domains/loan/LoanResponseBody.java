package fr.preto_back.domains.loan;

import fr.preto_back.domains.catalog.asset.AssetSummary;
import fr.preto_back.domains.user.UserSummary;
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
public class LoanResponseBody {
    Integer id;
    AssetSummary asset;
    UserSummary requester;
    UserSummary manager;
    LoanStatus status;
    LocalDateTime startDateAsked;
    LocalDateTime returnDateAsked;
    LocalDateTime actualCheckoutDate;
    LocalDateTime actualReturnDate;
    boolean isOverdue;
}
