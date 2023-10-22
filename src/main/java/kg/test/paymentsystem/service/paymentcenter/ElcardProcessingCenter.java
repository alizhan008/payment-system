package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.repository.ElcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ElcardProcessingCenter implements PaymentSystem {

    private final ElcardRepository elcardRepository;

    @Override
    public void cardIssue(Card card) {

    }

    @Override
    public void cardRefill(Card card) {

    }

    @Override
    public void chargeTheCard(Card card) {

    }
}
