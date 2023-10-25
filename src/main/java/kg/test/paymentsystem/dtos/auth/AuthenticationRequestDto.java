package kg.test.paymentsystem.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
public class AuthenticationRequestDto {

    @NotBlank
    @NotEmpty
    private String email;

    @NotBlank
    @NotEmpty
    private String password;

}
