package fr.preto_back.domains.loan;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.domains.catalog.asset.AssetService;
import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
import fr.preto_back.domains.catalog.category.Category;
import fr.preto_back.domains.catalog.category.CategoryRepository;
import fr.preto_back.domains.resa.ReservationRequest;
import fr.preto_back.domains.resa.ReservationRequestBody;
import fr.preto_back.domains.resa.ReservationRequestRepository;
import fr.preto_back.domains.resa.ReservationRequestService;
import fr.preto_back.domains.resa.ReservationRequestStatus;
import fr.preto_back.domains.user.Role;
import fr.preto_back.domains.user.User;
import fr.preto_back.domains.user.UserRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.NotActiveLoanStatusException;
import fr.preto_back.shared.exception.NotPendingCheckoutLoanStatusException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
@Transactional
public class TestLoanService {
    // Repositories
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AssetCopyRepository assetCopyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRequestRepository reservationRequestRepository;

    @Autowired
    private LoanRepository loanRepository;

    // Services
    @Autowired
    private ReservationRequestService reservationRequestService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private LoanService loanService;

    // Data that will be setup before tests and cleared after
    private Asset asset;
    private AssetCopy assetCopy;
    private User requester;
    private User manager;
    private ReservationRequestBody reservationRequestBody;
    private ReservationRequest reservationRequest;
    private Loan loan;

    // TODO : extract to make it reusable (also used in TestReservationRequestService)
    @BeforeEach
    void setup() {
        Category category = categoryRepository.save(Category.builder()
                .name("TEST")
                .build());

        asset = assetRepository.save(Asset.builder()
                .title("Vidéoprojecteur Philips NeoPix 160 Smart")
                .description("Full HD (1080p")
                .imageUrl(null)
                .category(category)
                .build());

        assetCopy = assetCopyRepository.save(AssetCopy.builder()
                .asset(asset)
                .state(AssetCopyState.NEW)
                .build());

        requester = userRepository.save(User.builder()
                .email("requester@test.com")
                .password("test")
                .firstName("Requester")
                .lastName("Test")
                .role(Role.USER)
                .build());

        manager = userRepository.save(User.builder()
                .email("manager@test.com")
                .password("test")
                .firstName("Manager")
                .lastName("Test")
                .role(Role.MANAGER)
                .build());

        reservationRequestBody = ReservationRequestBody.builder()
                .startDate(LocalDateTime.now().plusDays(1))
                .returnDate(LocalDateTime.now().plusDays(2))
                .build();

        reservationRequest = reservationRequestRepository.save(ReservationRequest.builder()
                .assetCopy(assetCopy)
                .startDateAsked(reservationRequestBody.getStartDate())
                .returnDateAsked(reservationRequestBody.getReturnDate())
                .requester(requester)
                .manager(manager)
                .status(ReservationRequestStatus.PENDING)
                .build());

        loan = loanRepository.save(Loan.builder()
                .actualCheckoutDate(null)
                .actualReturnDate(null)
                .reservationRequest(reservationRequest)
                .build());
    }

    @Test
    void testProcessLoanCheckout_KO_LOAN_NOT_PENDING_CHECKOUT() {
        // Set loan ACTIVE status, which is invalid, as loan must be in PENDING_CHECKOUT status to process checkout
        loan.setStatus(LoanStatus.ACTIVE);

        // Simulate reservation request approval because it is mandatory to process loan
        reservationRequest.setStatus(ReservationRequestStatus.APPROVED);

        log.info("testProcessLoanCheckout_KO_LOAN_NOT_PENDING_CHECKOUT loan={}", loan);

        // processLoanCheckout method should throw exception because invalid loan status
        Assertions.assertThatThrownBy(() -> loanService.processLoanCheckout(manager.getId(), loan.getId()))
                .isInstanceOf(NotPendingCheckoutLoanStatusException.class)
                .hasMessageContaining(ApiCode.LOAN_NOT_PENDING_CHECKOUT.getMessage());
    }

    @Test
    void testProcessLoanCheckout_OK() {
        log.info("testProcessLoanCheckout_OK BEFORE RESULT loan={}", loan);

        // Simulate reservation request approval because it is mandatory to process loan
        reservationRequest.setStatus(ReservationRequestStatus.APPROVED);

        // Call processLoanCheckout method from loan service
        LoanResponseBody result = loanService.processLoanCheckout(manager.getId(), loan.getId());

        log.info("testProcessLoanCheckout_OK AFTER RESULT loan={}", loan);

        // Check that loan status has been updated
        Assertions.assertThat(result.getStatus()).isEqualTo(LoanStatus.ACTIVE);
        // Check that actualCheckoutDate has been updated
        Assertions.assertThat(result.getActualCheckoutDate()).isNotNull();
    }

    @Test
    void testProcessReturnLoan_OK() {
        log.info("testProcessReturnLoan_OK BEFORE RESULT loan={}", loan);

        // Set loan status to ACTIVE, which is the only acceptable status
        loan.setStatus(LoanStatus.ACTIVE);

        // Simulate reservation request approval because it is mandatory to process loan
        reservationRequest.setStatus(ReservationRequestStatus.APPROVED);

        // Set request body
        ProcessReturnLoanRequestBody body = ProcessReturnLoanRequestBody.builder()
                .copyState(AssetCopyState.ACCEPTABLE)
                .build();

        // Call processLoanReturn and store result in variable
        LoanResponseBody result = loanService.processLoanReturn(manager.getId(), loan.getId(), body);

        log.info("testProcessReturnLoan_OK AFTER SERVICE METHOD CALL loan={}", loan);

        // Check that loan status has changed
        Assertions.assertThat(result.getStatus()).isEqualTo(LoanStatus.RETURNED);
        // Check that return date has been updated (was null)
        Assertions.assertThat(result.getActualReturnDate()).isNotNull();
        // Check that copy state has been updated
        Assertions.assertThat(result.getAsset().getState()).isEqualTo(body.getCopyState());
    }

    @Test
    void testProcessLoanReturn_KO_Loan_Not_Active() {
        // Simulate reservation request approval because it is mandatory to process loan
        reservationRequest.setStatus(ReservationRequestStatus.APPROVED);

        // Set request body
        ProcessReturnLoanRequestBody body = ProcessReturnLoanRequestBody.builder()
                .copyState(AssetCopyState.ACCEPTABLE)
                .build();

        log.info("testProcessLoanReturn_KO_Loan_Not_Active loan={}", loan);

        // processLoanReturn method should throw exception because invalid loan status -> can only process ACTIVE loan
        Assertions.assertThatThrownBy(() -> loanService.processLoanReturn(manager.getId(), loan.getId(), body))
                .isInstanceOf(NotActiveLoanStatusException.class)
                .hasMessageContaining(ApiCode.LOAN_NOT_ACTIVE.getMessage());
    }
}
