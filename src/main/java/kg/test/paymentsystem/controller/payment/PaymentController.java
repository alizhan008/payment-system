package kg.test.paymentsystem.controller.payment;

import jakarta.validation.constraints.NotBlank;
import kg.test.paymentsystem.dto.CardRequestDto;
import kg.test.paymentsystem.service.issue.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/v1")
public class PaymentController {

    private final CardService issueService;

    public void cardIssue(@RequestBody @NotBlank CardRequestDto cardRequestDto){
       issueService.issueCard(cardRequestDto);
    }

}
