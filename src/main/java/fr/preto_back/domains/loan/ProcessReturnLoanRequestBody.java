package fr.preto_back.domains.loan;

import fr.preto_back.domains.catalog.assetcopy.AssetCopyState;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class ProcessReturnLoanRequestBody {
    @NotNull(message = "{VALIDATION_COPY_STATE_REQUIRED}")
    private AssetCopyState copyState;
}
