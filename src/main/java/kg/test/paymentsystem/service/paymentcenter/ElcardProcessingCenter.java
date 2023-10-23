package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.Elcard;
import kg.test.paymentsystem.entity.user.User;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.ElcardRepository;
import kg.test.paymentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
@Transactional
@RequiredArgsConstructor
public class ElcardProcessingCenter implements PaymentSystem {

    private final ElcardRepository elcardRepository;

    private final UserRepository userRepository;

    @Override
    public Integer generateRandom16DigitNumber() {
        Random random = new Random();
        Integer number = null;
        for (int i = 0; i < 16; i++) {
            number = random.nextInt(10);
        }
        return number;

    }

    @Override
    public void cardIssue(Card card, User user) {
        var elcard = new Elcard(card);
        elcard.setIssueDate(LocalDate.now());
        elcard.setCardNumber(generateRandom16DigitNumber());
        elcard.setUser(user);

        elcardRepository.save(elcard);
        user.getCards().add(elcard);
        userRepository.save(user);

    }

    @Override
    public void cardRefill(Card card) {
        var elcard = elcardRepository.findByCardNumber(card.getCardNumber());

        if (elcard != null) {
            BigDecimal currentBalance = elcard.getBalance();
            BigDecimal refillAmount = card.getBalance();

            BigDecimal newBalance = currentBalance.add(refillAmount);
            elcard.setBalance(newBalance);

            elcardRepository.save(elcard);
        } else {
            throw new CardNotFoundException("Карта не найдена");
        }

    }

    @Override
    public void chargeTheCard(Card card) {
        try {
            var elcard = elcardRepository.findByCardNumber(card.getCardNumber());
            if (elcard != null) {
                BigDecimal currentBalance = elcard.getBalance();

                if (currentBalance.compareTo(card.getBalance()) >= 0) {
                    BigDecimal newBalance = currentBalance.subtract(card.getBalance());
                    elcard.setBalance(newBalance);

                    elcardRepository.save(elcard);
                } else {
                    throw new InsufficientBalanceException("Недостаточно средств на карте");
                }
            } else {
                throw new CardNotFoundException("Карта не найдена");
            }
        } catch (InsufficientBalanceException e) {
            throw new InsufficientBalanceException("Недостаточно средств");

        } catch (CardNotFoundException e) {
            throw new CardNotFoundException("Карта не найдена");
        }

    }

}
