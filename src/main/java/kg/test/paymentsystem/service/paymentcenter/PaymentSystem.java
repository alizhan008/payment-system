package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.entity.card.Card;

public interface PaymentSystem {
    Long generateUnique16DigitNumber();
    CardIssueResponseDto cardIssue(Card card, String userEmail);
    void cardRefill(Card card);
    void chargeTheCard(Card card);

}
