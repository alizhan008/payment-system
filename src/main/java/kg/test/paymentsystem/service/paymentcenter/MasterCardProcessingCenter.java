package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.MasterCard;
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
public class MasterCardProcessingCenter extends AbstractPaymentSystem {

    private final MasterCardRepository masterCardRepository;

    private final UserRepository userRepository;

    private static final BigDecimal AMOUNT = new BigDecimal(1000); //Симуляция остатка на счету

    @Override
    public Long generateUnique16DigitNumber() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            digits.add(i);
        }

        Collections.shuffle(digits);

        StringBuilder uniqueNumber = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            uniqueNumber.append(digits.get(i));
        }
        return Long.parseLong(uniqueNumber.toString());
    }

    @Override
    public CardIssueResponseDto cardIssue(Card card, String userEmail) {
        var masterCard = new MasterCard(card);
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        masterCard.setIssueDate(LocalDate.now());
        masterCard.setCardNumber(generateUnique16DigitNumber());
        masterCard.setBalance(AMOUNT);
        masterCard.setUser(user);

        masterCardRepository.save(masterCard);
        user.getMasterCards().add(masterCard);
        userRepository.save(user);

         return CardIssueResponseDto.builder()
                .bankName(masterCard.getBankName())
                .type(masterCard.getType())
                .cardNumber(masterCard.getCardNumber())
                .issueDate(masterCard.getIssueDate())
                .build();
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
