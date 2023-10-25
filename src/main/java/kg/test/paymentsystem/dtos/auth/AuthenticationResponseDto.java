package kg.test.paymentsystem.dtos.auth;

import lombok.*;

@Getter
@Setter
@Builder
public class AuthenticationResponseDto {
    private String token;
}
