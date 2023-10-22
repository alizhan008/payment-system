package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;

public interface PaymentSystem {
    void cardIssue(Card card);
    void cardRefill(Card card);
    void chargeTheCard(Card card);
}
