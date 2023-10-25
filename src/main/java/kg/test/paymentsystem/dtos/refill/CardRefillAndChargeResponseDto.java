package kg.test.paymentsystem.dtos.refill;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CardRefillAndChargeResponseDto {

    private BigDecimal balance;

}
