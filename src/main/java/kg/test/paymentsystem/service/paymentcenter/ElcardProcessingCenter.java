package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.Elcard;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.ElcardRepository;
import kg.test.paymentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ElcardProcessingCenter extends AbstractPaymentSystem {

    private final ElcardRepository elcardRepository;

    private final UserRepository userRepository;

    private static final BigDecimal AMOUNT = new BigDecimal(500); //Симуляция остатка на счету

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
        var elcard = new Elcard(card);
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        elcard.setIssueDate(LocalDate.now());
        elcard.setCardNumber(generateUnique16DigitNumber());
        elcard.setBalance(AMOUNT);
        elcard.setUser(user);

        elcardRepository.save(elcard);
        user.getElcards().add(elcard);
        userRepository.save(user);

        return CardIssueResponseDto.builder()
                .bankName(elcard.getBankName())
                .type(elcard.getType())
                .cardNumber(elcard.getCardNumber())
                .issueDate(elcard.getIssueDate())
                .build();
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
