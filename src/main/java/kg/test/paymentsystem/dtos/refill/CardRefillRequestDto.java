package kg.test.paymentsystem.dtos.refill;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CardRefillRequestDto {

    @NotBlank
    private Long cardNumber;

    @NotBlank
    private BigDecimal balance;

    @NotBlank
    private String type;

}
