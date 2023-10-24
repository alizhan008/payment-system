package kg.test.paymentsystem.dtos.issue;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
public class CardIssueResponseDto {

    private String bankName;
    private String type;
    private Long cardNumber;
    private LocalDate issueDate;

}
