package fr.preto_back.domains.loan;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/loans")
@AllArgsConstructor
public class LoanRestController {
    private final LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LoanResponseBody>>> getAllLoans() {
        // TODO : replace with auth user
        Integer managerId = 2;

        List<LoanResponseBody> loans = loanService.findAllLoans(managerId);
        ApiResponse<List<LoanResponseBody>> response = ApiResponse.success(ApiCode.LOANS_FOUND_SUCCESS.name(), ApiCode.LOANS_FOUND_SUCCESS.getMessage(), loans);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanResponseBody>> getLoanById(@PathVariable String id) {
        // TODO : replace with auth user
        Integer managerId = 2;

        LoanResponseBody loan = loanService.findById(managerId, Integer.parseInt(id.trim()));
        ApiResponse<LoanResponseBody> response = ApiResponse.success(ApiCode.LOAN_FOUND_SUCCESS.name(), ApiCode.LOAN_FOUND_SUCCESS.getMessage(), loan);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<ApiResponse<LoanResponseBody>> checkoutLoan(@PathVariable String id) {
        // TODO : replace with auth user
        Integer managerId = 2;

        LoanResponseBody loan = loanService.processLoanCheckout(managerId, Integer.parseInt(id.trim()));
        ApiResponse<LoanResponseBody> response = ApiResponse.success(ApiCode.LOAN_CHECKOUT_SUCCESS.name(), ApiCode.LOAN_CHECKOUT_SUCCESS.getMessage(), loan);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse<LoanResponseBody>> returnLoan(
            @PathVariable String id,
            @Valid @RequestBody ProcessReturnLoanRequestBody requestBody
    ) {
        // TODO : replace with auth user
        Integer managerId = 2;

        LoanResponseBody loan = loanService.processLoanReturn(managerId, Integer.parseInt(id.trim()), requestBody);
        ApiResponse<LoanResponseBody> response = ApiResponse.success(ApiCode.LOAN_RETURN_SUCCESS.name(), ApiCode.LOAN_RETURN_SUCCESS.getMessage(), loan);

        return ResponseEntity.ok(response);
    }
}
