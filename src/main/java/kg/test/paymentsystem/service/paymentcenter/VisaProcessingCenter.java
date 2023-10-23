package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.MasterCard;
import kg.test.paymentsystem.entity.card.Visa;
import kg.test.paymentsystem.entity.user.User;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.UserRepository;
import kg.test.paymentsystem.repository.VisaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
@Transactional
@RequiredArgsConstructor
public class VisaProcessingCenter implements PaymentSystem {

    private final VisaRepository visaRepository;

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
        var visaCard = new Visa(card);
        visaCard.setIssueDate(LocalDate.now());
        visaCard.setCardNumber(generateRandom16DigitNumber());
        visaCard.setUser(user);

        visaRepository.save(visaCard);
        user.getCards().add(visaCard);
        userRepository.save(user);

    }

    @Override
    public void cardRefill(Card card) {
        var visaCard = visaRepository.findByCardNumber(card.getCardNumber());

        if (visaCard != null) {
            BigDecimal currentBalance = visaCard.getBalance();
            BigDecimal refillAmount = card.getBalance();

            BigDecimal newBalance = currentBalance.add(refillAmount);
            visaCard.setBalance(newBalance);

            visaRepository.save(visaCard);
        } else {
            throw new CardNotFoundException("Карта не найдена");
        }

    }

    @Override
    public void chargeTheCard(Card card) {
        try {
            var visaCard = visaRepository.findByCardNumber(card.getCardNumber());
            if (visaCard != null) {
                BigDecimal currentBalance = visaCard.getBalance();

                if (currentBalance.compareTo(card.getBalance()) >= 0) {
                    BigDecimal newBalance = currentBalance.subtract(card.getBalance());
                    visaCard.setBalance(newBalance);

                    visaRepository.save(visaCard);
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
