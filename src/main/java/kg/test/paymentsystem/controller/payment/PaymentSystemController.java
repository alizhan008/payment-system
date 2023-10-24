package kg.test.paymentsystem.controller.payment;

import jakarta.validation.Valid;
import kg.test.paymentsystem.dtos.issue.CardIssueRequestDto;
import kg.test.paymentsystem.dtos.refill.CardRefillRequestDto;
import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pay-system")
public class PaymentSystemController {

    private final CardService cardService;

    @PostMapping("/issue-card")
    public ResponseEntity<CardIssueResponseDto> cardIssue(@RequestBody @Valid CardIssueRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardService.issueCard(cardRequestDto));
    }

    @PostMapping("/card-refill")
    public ResponseEntity<?> cardRefill(@RequestBody CardRefillRequestDto cardRefillDto) {
        cardService.cardRefill(cardRefillDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
