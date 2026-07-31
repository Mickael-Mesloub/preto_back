package fr.preto_back.domains.user;

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
// This class regroups essential information about a user that front may need to access and display
// on screens (for reservation requests, loans...)
public class UserSummary {
    private String firstName;
    private String lastName;
    private String email;
}
