package fr.preto_back.domains.loan;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.domains.catalog.asset.AssetService;
import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
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
    @Autowired
    private LoanRepository loanRepository;

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
    void testProcessLoanCheckout_KO_Invalid_Loan_Status() {
        // Set loan ACTIVE status, which is invalid, as loan must be in PENDING_CHECKOUT status to process checkout
        loan.setStatus(LoanStatus.ACTIVE);
        log.info("testProcessLoanCheckout_KO_Invalid_Loan_Status loan={}", loan);

        // processLoanCheckout method should throw exception because invalid loan status
        Assertions.assertThatThrownBy(() -> loanService.processLoanCheckout(manager.getId(), reservationRequest.getId(), loan.getId()))
                .isInstanceOf(NotPendingCheckoutLoanStatusException.class)
                .hasMessageContaining(ApiCode.LOAN_NOT_PENDING_CHECKOUT.getMessage());
    }

    @Test
    void testProcessLoanCheckout_OK() {
        // TODO : write successful test
        log.info("testProcessLoanCheckout_OK loan={}", loan);
    }

    @Test
    void testProcessReturnLoan_OK() {
        // TODO : write successful test
    }

    // TODO : write processReturnLoan handled error test
}
