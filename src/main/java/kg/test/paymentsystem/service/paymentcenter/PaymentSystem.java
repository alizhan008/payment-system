package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.user.User;

public interface PaymentSystem {
    Integer generateRandom16DigitNumber();
    void cardIssue(Card card, User user);
    void cardRefill(Card card);
    void chargeTheCard(Card card);

}
