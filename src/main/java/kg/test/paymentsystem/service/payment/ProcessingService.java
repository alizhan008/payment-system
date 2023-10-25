package kg.test.paymentsystem.service.payment;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeResponseDto;
import kg.test.paymentsystem.entity.card.CardEntity;

import java.math.BigDecimal;

public interface ProcessingService {
    BigDecimal AMOUNT = new BigDecimal(0); // стартовый остаток счета
    Long generateUnique16DigitNumber();
    CardIssueResponseDto cardIssue(CardEntity card, String userEmail);
    void cardRefill(CardEntity card, String email);
    void chargeTheCard(CardEntity card, String email);
    CardRefillAndChargeResponseDto checkCardBalance(Long cardNumber);

}
