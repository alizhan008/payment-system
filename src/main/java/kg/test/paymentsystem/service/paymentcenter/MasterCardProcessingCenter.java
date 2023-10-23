package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.MasterCard;
import kg.test.paymentsystem.entity.user.User;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.MasterCardRepository;
import kg.test.paymentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Component
@Transactional
@RequiredArgsConstructor
public class MasterCardProcessingCenter implements PaymentSystem {

    private final MasterCardRepository masterCardRepository;

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
        var masterCard = new MasterCard(card);
        masterCard.setIssueDate(LocalDate.now());
        masterCard.setCardNumber(generateRandom16DigitNumber());
        masterCard.setUser(user);

        masterCardRepository.save(masterCard);
        user.getCards().add(masterCard);
        userRepository.save(user);

    }

    @Override
    public void cardRefill(Card card) {
        var masterCard = masterCardRepository.findByCardNumber(card.getCardNumber());

        if (masterCard != null) {
            BigDecimal currentBalance = masterCard.getBalance();
            BigDecimal refillAmount = card.getBalance();

            BigDecimal newBalance = currentBalance.add(refillAmount);
            masterCard.setBalance(newBalance);

            masterCardRepository.save(masterCard);
        } else {
            throw new CardNotFoundException("Карта не найдена");
        }

    }

    @Override
    public void chargeTheCard(Card card) {
        try {
            var masterCard = masterCardRepository.findByCardNumber(card.getCardNumber());
            if (masterCard != null) {
                BigDecimal currentBalance = masterCard.getBalance();

                if (currentBalance.compareTo(card.getBalance()) >= 0) {
                    BigDecimal newBalance = currentBalance.subtract(card.getBalance());
                    masterCard.setBalance(newBalance);

                    masterCardRepository.save(masterCard);
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
