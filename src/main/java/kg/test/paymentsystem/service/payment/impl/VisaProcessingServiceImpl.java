package kg.test.paymentsystem.service.payment.impl;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeResponseDto;
import kg.test.paymentsystem.entity.TransactionEntity;
import kg.test.paymentsystem.entity.card.CardEntity;
import kg.test.paymentsystem.entity.card.VisaEntity;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.TransactionRepository;
import kg.test.paymentsystem.repository.user.UserRepository;
import kg.test.paymentsystem.repository.card.VisaRepository;
import kg.test.paymentsystem.service.payment.ProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class VisaProcessingServiceImpl implements ProcessingService {

    private final VisaRepository visaRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

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
    public CardIssueResponseDto cardIssue(CardEntity card, String userEmail) {
        var visaCard = new VisaEntity(card);
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        visaCard.setIssueDate(LocalDate.now());
        visaCard.setCardNumber(generateUnique16DigitNumber());
        visaCard.setBalance(AMOUNT);
        visaCard.setUser(user);

        visaRepository.save(visaCard);
        user.getVisas().add(visaCard);
        userRepository.save(user);
        log.info("Карта: {} успешно выпущена!", visaCard.getCardNumber());

        return CardIssueResponseDto.builder()
                .bankName(visaCard.getBankName())
                .type(visaCard.getType())
                .cardNumber(visaCard.getCardNumber())
                .issueDate(visaCard.getIssueDate())
                .build();
    }

    @Override
    public void cardRefill(CardEntity card, String email) throws CardNotFoundException {
        var usr = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));
        var visaCard = visaRepository.findByCardNumber(card.getCardNumber())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        if (!usr.getVisas().contains(visaCard)) {
            throw new CardNotFoundException("Карта не принадлежит данному пользователю!");
        }
        BigDecimal currentBalance = visaCard.getBalance();
        BigDecimal refillAmount = card.getBalance();

        BigDecimal newBalance = currentBalance.add(refillAmount);
        visaCard.setBalance(newBalance);

        visaRepository.save(visaCard);
        var transactionCheck = TransactionEntity.builder()
                .amountRefill(refillAmount)
                .bankName(visaCard.getBankName())
                .cardNumber(visaCard.getCardNumber())
                .userEmail(usr.getEmail())
                .build();
        transactionRepository.save(transactionCheck);
        log.info("Баланс карты: {} успешно пополнен на сумму {}", visaCard.getCardNumber(), refillAmount);
    }

    @Override
    public void chargeTheCard(CardEntity card, String email) throws InsufficientBalanceException, CardNotFoundException {
        var usr = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));
        var visaCard = visaRepository.findByCardNumber(card.getCardNumber())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));
        if (!usr.getVisas().contains(visaCard)) {
            throw new CardNotFoundException("Карта не принадлежит данному пользователю!");
        }
        BigDecimal currentBalance = visaCard.getBalance();

        if (currentBalance.compareTo(card.getBalance()) < 0) {
            throw new InsufficientBalanceException("Недостаточно средств на карте!");
        }
        BigDecimal newBalance = currentBalance.subtract(card.getBalance());
        visaCard.setBalance(newBalance);

        visaRepository.save(visaCard);
        var transactionCheck = TransactionEntity.builder()
                .amountCharge(card.getBalance())
                .bankName(visaCard.getBankName())
                .cardNumber(visaCard.getCardNumber())
                .build();
        transactionRepository.save(transactionCheck);
        log.info("Списание с карты: {} на сумму: {} выполнено успешно!", card.getCardNumber(), card.getBalance());
    }

    @Override
    public CardRefillAndChargeResponseDto checkCardBalance(Long cardNumber) throws CardNotFoundException {
        var visaCard = visaRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        return CardRefillAndChargeResponseDto.builder()
                .balance(visaCard.getBalance())
                .build();
    }

}
