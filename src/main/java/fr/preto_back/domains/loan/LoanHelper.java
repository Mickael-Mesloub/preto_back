package fr.preto_back.domains.loan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LoanHelper {
    // Checks if asset is returned with overdue
    public boolean checkIsLoanOverdue(LocalDateTime plannedReturnDate) {
        return plannedReturnDate.isBefore(LocalDateTime.now());
    }
}
