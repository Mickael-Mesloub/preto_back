package fr.preto_back.domains.loan;

import fr.preto_back.domains.catalog.asset.AssetSummary;
import fr.preto_back.domains.resa.ReservationRequest;
import fr.preto_back.domains.user.UserSummary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoanMapper {
    // Maps Loan entity to dto for loan checkout
    public LoanResponseBody toDto(
            Loan loan,
            AssetSummary assetSummary,
            UserSummary requesterSummary,
            UserSummary managerSummary,
            ReservationRequest resa
    ) {
        return LoanResponseBody.builder()
                .id(loan.getId())
                .asset(assetSummary)
                .requester(requesterSummary)
                .manager(managerSummary)
                .status(loan.getStatus())
                .startDateAsked(resa.getStartDateAsked())
                .returnDateAsked(resa.getReturnDateAsked())
                .actualCheckoutDate(loan.getActualCheckoutDate())
                .actualReturnDate(loan.getActualReturnDate())
                .build();
    }


    // Maps Loan entity to dto for loan return
    public LoanResponseBody toDto(
            Loan loan,
            AssetSummary assetSummary,
            UserSummary requester,
            UserSummary manager,
            ReservationRequest resa,
            boolean isOverdue
    ) {
        return LoanResponseBody.builder()
                .id(loan.getId())
                .asset(assetSummary)
                .requester(requester)
                .manager(manager)
                .status(loan.getStatus())
                .startDateAsked(resa.getStartDateAsked())
                .returnDateAsked(resa.getReturnDateAsked())
                .actualCheckoutDate(loan.getActualCheckoutDate())
                .actualReturnDate(loan.getActualReturnDate())
                .isOverdue(isOverdue)
                .build();
    }


}
