package kg.test.paymentsystem.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CardRequestDto {

    private Long id;

    private String bankName;

    private String type;

    private Integer cardNumber;

    private LocalDate issueDate;

}
