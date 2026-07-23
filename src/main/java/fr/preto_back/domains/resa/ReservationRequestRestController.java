package fr.preto_back.domains.resa;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/reservation-requests")
@AllArgsConstructor
public class ReservationRequestRestController {
    private ReservationRequestService reservationRequestService;

    @PostMapping("/{assetId}/new")
    public ResponseEntity<ApiResponse<ReservationRequestDTO>> newReservationRequest(@PathVariable String assetId, @RequestBody ReservationRequestBody reservationRequestBody) {
        // TODO: replace with auth userId
        Integer userId = 3;
        ReservationRequestDTO reservationRequestDto = reservationRequestService.newReservationRequest(userId, Integer.parseInt(assetId.trim()), reservationRequestBody);
        ApiResponse<ReservationRequestDTO> response = ApiResponse.success(ApiCode.RESA_SAVE_SUCCESS.name(), ApiCode.RESA_SAVE_SUCCESS.getMessage(), reservationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
