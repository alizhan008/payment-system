package kg.test.paymentsystem.dtos.refill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CardRefillAndChargeRequestDto {

    @NotBlank
    @NotEmpty
    private Long cardNumber;

    @NotBlank
    @NotEmpty
    private BigDecimal balance;

    @NotBlank
    @NotEmpty
    private String type;

}
