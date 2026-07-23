package fr.preto_back.domains.resa;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyHelper;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
import fr.preto_back.domains.user.User;
import fr.preto_back.domains.user.UserRepository;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.NoAvailableCopyException;
import fr.preto_back.shared.exception.ResourceNotFoundException;
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

    public ReservationRequestDTO newReservationRequest(Integer requesterId, Integer assetId, @RequestBody ReservationRequestBody reservationRequestBody) {
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
        return mapper.toDto(reservationRequest);
    }

}
