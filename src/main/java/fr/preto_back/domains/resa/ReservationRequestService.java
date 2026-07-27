package fr.preto_back.domains.resa;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyHelper;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
import fr.preto_back.domains.user.User;
import fr.preto_back.domains.user.UserRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.DeclineReasonMissingException;
import fr.preto_back.shared.exception.InvalidDecisionException;
import fr.preto_back.shared.exception.NoAvailableCopyException;
import fr.preto_back.shared.exception.NotPendingReservationRequestException;
import fr.preto_back.shared.exception.ResourceNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class ReservationRequestService {
    private final ReservationRequestRepository reservationRequestRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final AssetCopyHelper  assetCopyHelper;
    private final ReservationRequestMapper mapper;
    private final AssetCopyRepository assetCopyRepository;

    public ReservationRequestResponseBody newReservationRequest(Integer requesterId, Integer assetId, @RequestBody ReservationRequestBody reservationRequestBody) {
        log.info("Received new reservation request, assetId={}, reservationRequest={}", assetId,  reservationRequestBody);

        // TODO: startDate and returnDate validation -> returnDate cannot be before or equal to startDate

        // Check if user exists with requesterId provided
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + requesterId));

        // TODO: check requester's current pending reservation requests. Throw error if 5 pending reservation requests.

        // Check if asset exists with assetId provided
        Asset existingAsset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + assetId));

        // Retrieve all the existing asset copies
        List<AssetCopy> copies = assetCopyRepository.findAllByAssetId(existingAsset.getId());

        // Loop through copies and check if there is an available copy of this asset in range between startDate and returnDate AND in a decent physical state
        Optional<AssetCopy> firstAvailableCopy = assetCopyHelper.getFirstAvailableAssetCopy(copies);

        // Throw exception if no available copy was found
        if (firstAvailableCopy.isEmpty()) {
            throw new NoAvailableCopyException(ApiCode.ASSET_COPY_NO_AVAILABLE_COPY.getMessage() + " for asset with id " + assetId);
        }

        // Save new reservation request in base
        ReservationRequest reservationRequest = reservationRequestRepository.save(ReservationRequest.builder()
                .assetCopy(firstAvailableCopy.get())
                .startDateAsked(reservationRequestBody.startDate)
                .returnDateAsked(reservationRequestBody.returnDate)
                .requester(requester)
                .manager(null)
                .build());

        // Map entity to dto and send dto to controller
        return mapper.toDto(reservationRequest, ReservationRequestStatus.PENDING, null);
    }

    // TODO : check auth user role == MANAGER || ADMIN
    public List<ReservationRequestResponseBody> findAllReservationRequests() {
        List<ReservationRequest> reservationRequests = reservationRequestRepository.findAll();

        return reservationRequests.stream()
                .map(mapper::toDto)
                .toList();
    }

    // TODO : check auth user role == MANAGER || ADMIN
    public List<ReservationRequestResponseBody> findReservationRequestsByStatus(ReservationRequestStatus status) {
        List<ReservationRequest> reservationRequests = reservationRequestRepository.findReservationRequestByStatusIs(status);

        return reservationRequests.stream()
                .map(mapper::toDto)
                .toList();
    }

    // Process reservation request
    public ReservationRequestResponseBody processReservationRequest(Integer managerId, Integer resaId, ProcessReservationRequestBody body) {
        log.info("processReservationRequest, managerId={}, resaId={}, body={}", managerId, resaId, body);

        // Retrieve user with managerId => if not found, exception
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + managerId));

        // TODO: check role == MANAGER

        // Retrieve reservationRequest with resaId => if not found, exception
        ReservationRequest reservationRequest = reservationRequestRepository.findById(resaId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.RESA_NOT_FOUND, ApiCode.RESA_NOT_FOUND.getMessage() + " with id " + resaId));

        // Retrieve requester with requesterId from existingReservationRequest
        User requester = userRepository.findById(reservationRequest.getRequester().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + reservationRequest.getRequester().getId()));

        // Check status == PENDING => if not, exception
        boolean isPending = reservationRequest.getStatus().equals(ReservationRequestStatus.PENDING);

        if(!isPending) {
            throw new NotPendingReservationRequestException();
        }

        // Check if decision is either APPROVED or DECLINED => if not, exception
        if (body.getDecision() != ReservationRequestDecision.APPROVED && body.getDecision() != ReservationRequestDecision.DECLINED) {
            throw new InvalidDecisionException();
        }

        // Check if decision == DECLINED
            // If so, check that declineReason not null/blank. => If so, exception
        if (body.getDecision() == ReservationRequestDecision.DECLINED
                && StringUtils.isBlank(body.getDeclineReason())) {
            throw new DeclineReasonMissingException();
        }

        // Update reservationRequest with manager, status APPROVED or DECLINED, declineReason
        reservationRequest.setStatus(body.getDecision().toStatus());
        reservationRequest.setManager(manager);
        reservationRequest.setDeclineReason(body.getDeclineReason());


        // TODO : If APPROVED, create LoanDTO

        // Save ReservationRequest in base
        ReservationRequest updatedResa = reservationRequestRepository.save(reservationRequest);

        // Return updated DTO
        return mapper.toDto(updatedResa, updatedResa.getStatus(), updatedResa.getDeclineReason());
    }

}
