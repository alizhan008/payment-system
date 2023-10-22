package kg.test.paymentsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class CardResponseDto {

    private String bankName;

    private String type;

    private Integer cardNumber;

    private LocalDate issueDate;

}
