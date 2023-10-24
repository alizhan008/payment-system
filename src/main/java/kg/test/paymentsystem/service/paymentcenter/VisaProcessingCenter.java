package kg.test.paymentsystem.service.paymentcenter;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.Visa;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.UserRepository;
import kg.test.paymentsystem.repository.VisaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class VisaProcessingCenter extends AbstractPaymentSystem {

    private final VisaRepository visaRepository;

    private final UserRepository userRepository;

    private static final BigDecimal AMOUNT = new BigDecimal(1500); //Симуляция остатка на счету

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
        try {
            var visaCard = new Visa(card);
            var user = userRepository.findByEmail(userEmail).orElseThrow();
            visaCard.setIssueDate(LocalDate.now());
            visaCard.setCardNumber(generateUnique16DigitNumber());
            visaCard.setBalance(AMOUNT);
            visaCard.setUser(user);

            visaRepository.save(visaCard);
            user.getVisas().add(visaCard);
            userRepository.save(user);

            return CardIssueResponseDto.builder()
                    .bankName(visaCard.getBankName())
                    .type(visaCard.getType())
                    .cardNumber(visaCard.getCardNumber())
                    .issueDate(visaCard.getIssueDate())
                    .build();
        }catch (UsernameNotFoundException unf){
            throw new UsernameNotFoundException("Пользователь не найден!");
        }
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
