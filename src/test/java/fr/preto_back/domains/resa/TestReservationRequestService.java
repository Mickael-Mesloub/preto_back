package fr.preto_back.domains.resa;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.domains.catalog.asset.AssetService;
import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
import fr.preto_back.domains.catalog.category.Category;
import fr.preto_back.domains.catalog.category.CategoryRepository;
import fr.preto_back.domains.user.Role;
import fr.preto_back.domains.user.User;
import fr.preto_back.domains.user.UserRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.DeclineReasonMissingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@Transactional
public class TestReservationRequestService {
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

    // Data that will be setup before tests and cleared after
    private Asset asset;
    private AssetCopy assetCopy;
    private User requester;
    private User manager;
    private ReservationRequestBody reservationRequestBody;
    private ReservationRequest reservationRequest;

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
    }

    @Test
    public void testNewReservationRequest_OK() {
        // Create new reservation request
        ReservationRequestResponseBody reservationRequestResponseBody = reservationRequestService.newReservationRequest(requester.getId(), asset.getId(), reservationRequestBody);

        // The new reservation should have been saved in based, so it should not be null
        Assertions.assertThat(reservationRequestResponseBody).isNotNull();
    }

    @Test
    public void testFindReservationRequestsByStatus_PENDING_OK() {
        // Create new reservation request (by default, status = PENDING)
        ReservationRequestResponseBody reservationRequestResponseBody = reservationRequestService.newReservationRequest(requester.getId(), asset.getId(), reservationRequestBody);

        // Retrieve all requests with status == PENDING
        List<ReservationRequestResponseBody> pendingRequests = reservationRequestService.findReservationRequestsByStatus(ReservationRequestStatus.PENDING);

        // The list should not be empty
        Assertions.assertThat(pendingRequests).isNotEmpty();

        // The new reservation should be in the list
        Assertions.assertThat(pendingRequests.stream().findFirst().get().getStatus()).isEqualTo(ReservationRequestStatus.PENDING);
    }

    @Test
    public void testProcessReservationRequest_OK() {
        // Instantiate ProcessReservationRequestBody -> when a MANAGER approves/declines a reservation request
        ProcessReservationRequestBody body = ProcessReservationRequestBody.builder()
                .decision(ReservationRequestDecision.APPROVED)
                .declineReason(null)
                .build();

        // Process the reservation request
        ReservationRequestResponseBody processedResa = reservationRequestService.processReservationRequest(manager.getId(), reservationRequest.getId(), body);

        // Manager is null by default when a new request is sent.
        // It should have been updated and should now contain manager's data
        Assertions.assertThat(processedResa.managerId).isEqualTo(manager.getId());

        // Resa status should have been updated from PENDING to APPROVED
        Assertions.assertThat(processedResa.status).isEqualTo(ReservationRequestStatus.APPROVED);
    }

    @Test
    public void testProcessReservationRequest_KO_Missing_Decline_Reason() {
        // Instantiate invalid body : with DECLINED status but missing required declineReason
        ProcessReservationRequestBody body = ProcessReservationRequestBody.builder()
                .decision(ReservationRequestDecision.DECLINED)
                .declineReason(null)
                .build();

        // An exception should be thrown because the request has been declined with no reason provided
        Assertions.assertThatThrownBy(() -> reservationRequestService.processReservationRequest(manager.getId(), reservationRequest.getId(), body))
                .isInstanceOf(DeclineReasonMissingException.class)
                .hasMessageContaining(ApiCode.RESA_DECLINED_MISSING_REASON.getMessage());
    }
}
