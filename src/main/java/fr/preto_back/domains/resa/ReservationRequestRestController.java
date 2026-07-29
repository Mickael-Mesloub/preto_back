package fr.preto_back.domains.resa;

import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/reservation-requests")
@AllArgsConstructor
public class ReservationRequestRestController {
    private ReservationRequestService reservationRequestService;

    @PostMapping("/asset/{assetId}/new-reservation-request")
    public ResponseEntity<ApiResponse<ReservationRequestResponseBody>> newReservationRequest(@PathVariable String assetId, @Valid @RequestBody ReservationRequestBody reservationRequestBody) {
        // TODO: replace with auth userId
        Integer userId = 3;

        ReservationRequestResponseBody reservationRequest = reservationRequestService.newReservationRequest(userId, Integer.parseInt(assetId.trim()), reservationRequestBody);
        ApiResponse<ReservationRequestResponseBody> response = ApiResponse.success(ApiCode.RESA_SAVE_SUCCESS.name(), ApiCode.RESA_SAVE_SUCCESS.getMessage(), reservationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ReservationRequestResponseBody>>> getAllReservationRequests() {
        List<ReservationRequestResponseBody> reservationRequests = reservationRequestService.findAllReservationRequests();
        ApiResponse<List<ReservationRequestResponseBody>> response = ApiResponse.success(ApiCode.RESAS_FOUND_SUCCESS.name(), ApiCode.RESAS_FOUND_SUCCESS.getMessage(), reservationRequests);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<ApiResponse<ReservationRequestResponseBody>> processReservationRequest(@PathVariable String id, @RequestBody ProcessReservationRequestBody body) {
        // TODO: replace with auth userId
        Integer managerId = 2;

        ReservationRequestResponseBody reservationRequest = reservationRequestService.processReservationRequest(managerId, Integer.parseInt(id.trim()), body);
        ApiResponse<ReservationRequestResponseBody> response = ApiResponse.success(ApiCode.RESA_PROCESS_SUCCESS.name(), ApiCode.RESA_PROCESS_SUCCESS.getMessage(), reservationRequest);

        return ResponseEntity.ok(response);

    }
}
