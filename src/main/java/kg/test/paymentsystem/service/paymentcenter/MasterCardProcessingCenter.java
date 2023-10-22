package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.repository.MasterCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MasterCardProcessingCenter implements PaymentSystem {

    private final MasterCardRepository masterCardRepository;

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
