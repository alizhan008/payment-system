package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.entity.card.Card;

public abstract class  AbstractPaymentSystem implements PaymentSystem {

    @Override
    public Long generateUnique16DigitNumber() {
        return null;
    }

    @Override
    public CardIssueResponseDto cardIssue(Card card, String userEmail) {
        return null;
    }

    @Override
    public void cardRefill(Card card) {
    }

    @Override
    public void chargeTheCard(Card card) {
    }

}
