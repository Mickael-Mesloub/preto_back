package fr.preto_back.domains.loan;

import fr.preto_back.domains.catalog.asset.Asset;
import fr.preto_back.domains.catalog.asset.AssetRepository;
import fr.preto_back.domains.catalog.asset.AssetSummary;
import fr.preto_back.domains.catalog.assetcopy.AssetCopy;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyRepository;
import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
import fr.preto_back.domains.resa.ReservationRequest;
import fr.preto_back.domains.resa.ReservationRequestRepository;
import fr.preto_back.domains.resa.ReservationRequestStatus;
import fr.preto_back.domains.user.User;
import fr.preto_back.domains.user.UserRepository;
import fr.preto_back.domains.user.UserSummary;
import fr.preto_back.shared.api_response.ApiCode;
import fr.preto_back.shared.exception.NotActiveLoanStatusException;
import fr.preto_back.shared.exception.NotPendingCheckoutLoanStatusException;
import fr.preto_back.shared.exception.ResaNotApprovedException;
import fr.preto_back.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final ReservationRequestRepository reservationRequestRepository;
    private final LoanHelper loanHelper;
    private final LoanMapper loanMapper;
    private final AssetRepository assetRepository;
    private final AssetCopyRepository assetCopyRepository;

    public List<LoanResponseBody> findAllLoans(Integer managerId) {
        // Check that user exists with managerId
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + managerId));

        List<Loan> loans = loanRepository.findAll();

        // Loop through loans and build dto for each loan
        return loans.stream()
                .map(l -> {
                    boolean isOverdue = loanHelper.checkIsLoanOverdue(l.getReservationRequest().getReturnDateAsked());

                    // Create summary of asset/copy information that will be stored in dto and sent in response
                    AssetSummary assetSummary = AssetSummary.builder()
                            .assetTitle(l.getReservationRequest().getAssetCopy().getAsset().getTitle())
                            .assetDescription(l.getReservationRequest().getAssetCopy().getAsset().getDescription())
                            .state(l.getReservationRequest().getAssetCopy().getState())
                            .build();

                    // Create user summary with requester information
                    UserSummary requesterSummary = UserSummary.builder()
                            .firstName(l.getReservationRequest().getRequester().getFirstName())
                            .lastName(l.getReservationRequest().getRequester().getLastName())
                            .email(l.getReservationRequest().getRequester().getEmail())
                            .build();

                    // Create user summary with manager information
                    UserSummary managerSummary = UserSummary.builder()
                            .firstName(l.getReservationRequest().getManager().getFirstName())
                            .lastName(l.getReservationRequest().getManager().getLastName())
                            .build();
                   return loanMapper.toDto(l, assetSummary, requesterSummary, managerSummary, l.getReservationRequest(), isOverdue);
                })
                .toList();
    }

    public LoanResponseBody findById(Integer managerId, Integer loanId) {
        // Check that user exists with managerId
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + managerId));

        // Check that loan exists with id
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.LOAN_NOT_FOUND, ApiCode.LOAN_NOT_FOUND.getMessage() + " with id " + loanId));


        // TODO : refactor -> code duplication
        // Check if loan is overdue
        boolean isOverdue = loanHelper.checkIsLoanOverdue(existingLoan.getReservationRequest().getReturnDateAsked());

        // Create summary of asset/copy information that will be stored in dto and sent in response
        AssetSummary assetSummary = AssetSummary.builder()
                .assetTitle(existingLoan.getReservationRequest().getAssetCopy().getAsset().getTitle())
                .assetDescription(existingLoan.getReservationRequest().getAssetCopy().getAsset().getDescription())
                .state(existingLoan.getReservationRequest().getAssetCopy().getState())
                .build();

        // Create user summary with requester information
        UserSummary requesterSummary = UserSummary.builder()
                .firstName(existingLoan.getReservationRequest().getRequester().getFirstName())
                .lastName(existingLoan.getReservationRequest().getRequester().getLastName())
                .email(existingLoan.getReservationRequest().getRequester().getEmail())
                .build();

        // Create user summary with manager information
        UserSummary managerSummary = UserSummary.builder()
                .firstName(existingLoan.getReservationRequest().getManager().getFirstName())
                .lastName(existingLoan.getReservationRequest().getManager().getLastName())
                .build();
        return loanMapper.toDto(existingLoan, assetSummary, requesterSummary, managerSummary, existingLoan.getReservationRequest(), isOverdue);
    }


    // Process loan checkout : when user comes physically to claim the asset he loaned => Manager processes the checkout
    public LoanResponseBody processLoanCheckout(Integer managerId, Integer loanId) {
        // Check that user exists with managerId
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + managerId));

        // Check that loan exists with id
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.LOAN_NOT_FOUND, ApiCode.LOAN_NOT_FOUND.getMessage() + " with id " + loanId));

        // Check existingLoan status
        // If not PENDING_CHECKOUT, throw exception => only "PENDING_CHECKOUT" loans can be checked out
        if (!existingLoan.getStatus().equals(LoanStatus.PENDING_CHECKOUT)) {
            throw new NotPendingCheckoutLoanStatusException();
        }

        // Check that resa exists with id
        ReservationRequest existingResa = reservationRequestRepository.findById(existingLoan.getReservationRequest().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.RESA_NOT_FOUND, ApiCode.RESA_NOT_FOUND.getMessage() + " with id " + existingLoan.getReservationRequest().getId()));

        // Check if resa APPROVED => if not, exception => Cannot process loan if reservation has not been approved yet
        if (existingResa.getStatus() != ReservationRequestStatus.APPROVED) {
            throw new ResaNotApprovedException();
        }

        // Check that asset exists with id
        Asset existingAsset = assetRepository.findById(existingResa.getAssetCopy().getAsset().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + existingResa.getAssetCopy().getAsset().getId()));

        // Set loan checkout datetime to now
        existingLoan.setActualCheckoutDate(LocalDateTime.now());

        // Update loan status
        existingLoan.setStatus(LoanStatus.ACTIVE);

        // Save updates in base
        Loan updatedLoan = loanRepository.save(existingLoan);

        // Create summary of asset/copy information that will be stored in dto and sent in response
        AssetSummary assetSummary = AssetSummary.builder()
                .assetTitle(existingAsset.getTitle())
                .assetDescription(existingAsset.getDescription())
                .state(existingResa.getAssetCopy().getState())
                .build();

        // Create user summary with requester information
        UserSummary requesterSummary = UserSummary.builder()
                .firstName(existingResa.getRequester().getFirstName())
                .lastName(existingResa.getRequester().getLastName())
                .email(existingResa.getRequester().getEmail())
                .build();

        // Create user summary with manager information
        UserSummary managerSummary = UserSummary.builder()
                .firstName(existingResa.getManager().getFirstName())
                .lastName(existingResa.getManager().getLastName())
                .build();

        // Map to dto and return it
        return loanMapper.toDto(updatedLoan, assetSummary, requesterSummary, managerSummary, existingResa);
    }

    // TODO : refactor methods above and below -> loads of duplicated code

    // Process loan return : when user comes physically to return the asset he loaned => Manager processes the return
    public LoanResponseBody processLoanReturn(Integer managerId, Integer loanId, ProcessReturnLoanRequestBody requestBody) {
        // Check that user exists with managerId
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.USER_NOT_FOUND, ApiCode.USER_NOT_FOUND.getMessage() + " with id " + managerId));

        // Check that loan exists with id
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.LOAN_NOT_FOUND, ApiCode.LOAN_NOT_FOUND.getMessage() + " with id " + loanId));

        // Check existingLoan status
        // If not ACTIVE, throw exception => only "ACTIVE" loans can be returned
        if (!existingLoan.getStatus().equals(LoanStatus.ACTIVE)) {
            throw new NotActiveLoanStatusException();
        }

        // Check that resa exists with id
        ReservationRequest existingResa = reservationRequestRepository.findById(existingLoan.getReservationRequest().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.RESA_NOT_FOUND, ApiCode.RESA_NOT_FOUND.getMessage() + " with id " + existingLoan.getReservationRequest().getId()));

        // Check if resa APPROVED => if not, exception => Cannot process loan if reservation has not been approved yet
        if (existingResa.getStatus() != ReservationRequestStatus.APPROVED) {
            throw new ResaNotApprovedException();
        }

        // Check that asset exists with id
        Asset existingAsset = assetRepository.findById(existingResa.getAssetCopy().getAsset().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_NOT_FOUND, ApiCode.ASSET_NOT_FOUND.getMessage() + " with id " + existingResa.getAssetCopy().getAsset().getId()));

        // Check that asset copy exists with id
        AssetCopy existingAssetCopy =  assetCopyRepository.findById(existingResa.getAssetCopy().getId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiCode.ASSET_COPY_NOT_FOUND, ApiCode.ASSET_COPY_NOT_FOUND.getMessage() + " with id " + existingResa.getAssetCopy().getId()));

        // Check if overdue (create helper method for this)
        // If overdue, loanResponseBody.isOverdue() = true
        boolean isOverdue = loanHelper.checkIsLoanOverdue(existingResa.getReturnDateAsked());

        // Update loan status
        // If copy state == LOST, update existingLoan.status to LOST as well
        if (requestBody.getCopyState().equals(AssetCopyState.LOST)) {
            existingLoan.setStatus(LoanStatus.LOST);
        } else {
            // else, update existingLoan.status to RETURNED
            existingLoan.setStatus(LoanStatus.RETURNED);
        }

        // Update copy state with state received from request body
        existingAssetCopy.setState(requestBody.getCopyState());

        // Save updated copy in base
        assetCopyRepository.save(existingAssetCopy);

        // Update actual return date to now
        existingLoan.setActualReturnDate(LocalDateTime.now());

        // Save loan updates in base
        Loan updatedLoan = loanRepository.save(existingLoan);

        // Create summary of asset/copy information that will be stored in dto and sent in response
        AssetSummary assetSummary = AssetSummary.builder()
                .assetTitle(existingAsset.getTitle())
                .assetDescription(existingAsset.getDescription())
                .state(existingResa.getAssetCopy().getState())
                .build();

        // Create user summary with requester information
        UserSummary requesterSummary = UserSummary.builder()
                .firstName(existingResa.getRequester().getFirstName())
                .lastName(existingResa.getRequester().getLastName())
                .email(existingResa.getRequester().getEmail())
                .build();

        // Create user summary with manager information
        UserSummary managerSummary = UserSummary.builder()
                .firstName(existingResa.getManager().getFirstName())
                .lastName(existingResa.getManager().getLastName())
                .build();

        // Map to dto and return it
        return loanMapper.toDto(updatedLoan, assetSummary, requesterSummary, managerSummary, existingResa, isOverdue);
    }
}
