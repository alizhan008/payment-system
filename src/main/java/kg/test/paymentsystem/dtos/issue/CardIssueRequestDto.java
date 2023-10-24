package kg.test.paymentsystem.dtos.issue;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardIssueRequestDto {

    @NotBlank
    private String bankName;

    @NotBlank
    private String type;

}
