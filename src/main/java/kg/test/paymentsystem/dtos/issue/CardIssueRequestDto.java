package kg.test.paymentsystem.dtos.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CardIssueRequestDto {

    @NotBlank
    @NotEmpty
    private String bankName;

    @NotBlank
    @NotEmpty
    private String type;

}
