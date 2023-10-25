package kg.test.paymentsystem.service.payment.impl;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeResponseDto;
import kg.test.paymentsystem.entity.TransactionEntity;
import kg.test.paymentsystem.entity.card.CardEntity;
import kg.test.paymentsystem.entity.card.ElcardEntity;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.card.ElcardRepository;
import kg.test.paymentsystem.repository.TransactionRepository;
import kg.test.paymentsystem.repository.user.UserRepository;
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
public class ElcardProcessingServiceImpl implements ProcessingService {

    private final ElcardRepository elcardRepository;
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
        for (int i = 0; i < 10; i++) {
            uniqueNumber.append(digits.get(i));
        }
        return Long.parseLong(uniqueNumber.toString());
    }

    @Override
    public CardIssueResponseDto cardIssue(CardEntity card, String userEmail) {
        var elcard = new ElcardEntity(card);
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        elcard.setIssueDate(LocalDate.now());
        elcard.setCardNumber(generateUnique16DigitNumber());
        elcard.setBalance(AMOUNT);
        elcard.setUser(user);

        elcardRepository.save(elcard);
        user.getElcards().add(elcard);
        userRepository.save(user);
        log.info("Карта: {} успешно выпущена!", elcard.getCardNumber());

        return CardIssueResponseDto.builder()
                .bankName(elcard.getBankName())
                .type(elcard.getType())
                .cardNumber(elcard.getCardNumber())
                .issueDate(elcard.getIssueDate())
                .build();
    }

    @Override
    public void cardRefill(CardEntity card, String email) throws CardNotFoundException {
        var usr = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));
        var elcard = elcardRepository.findByCardNumber(card.getCardNumber())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        if (!usr.getElcards().contains(elcard)) {
            throw new CardNotFoundException("Карта не принадлежит данному пользователю!");
        }
        BigDecimal currentBalance = elcard.getBalance();
        BigDecimal refillAmount = card.getBalance();

        BigDecimal newBalance = currentBalance.add(refillAmount);
        elcard.setBalance(newBalance);

        elcardRepository.save(elcard);
        var transactionCheck = TransactionEntity.builder()
                .amountRefill(refillAmount)
                .bankName(elcard.getBankName())
                .cardNumber(elcard.getCardNumber())
                .userEmail(usr.getEmail())
                .build();
        transactionRepository.save(transactionCheck);
        log.info("Баланс карты: {} успешно пополнен на сумму {}", elcard.getCardNumber(), refillAmount);
    }

    @Override
    public void chargeTheCard(CardEntity card, String email) throws InsufficientBalanceException, CardNotFoundException {
        var usr = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));
        var elcard = elcardRepository.findByCardNumber(card.getCardNumber())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        if (!usr.getElcards().contains(elcard)) {
            throw new CardNotFoundException("Карта не принадлежит данному пользователю!");
        }
        BigDecimal currentBalance = elcard.getBalance();

        if (currentBalance.compareTo(card.getBalance()) < 0) {
            throw new InsufficientBalanceException("Недостаточно средств на карте!");
        }
        BigDecimal newBalance = currentBalance.subtract(card.getBalance());
        elcard.setBalance(newBalance);

        elcardRepository.save(elcard);
        var transactionCheck = TransactionEntity.builder()
                .amountCharge(card.getBalance())
                .bankName(elcard.getBankName())
                .cardNumber(elcard.getCardNumber())
                .build();
        transactionRepository.save(transactionCheck);
        log.info("Списание с карты: {} на сумму: {} выполнено успешно!", card.getCardNumber(), card.getBalance());
    }

    @Override
    public CardRefillAndChargeResponseDto checkCardBalance(Long cardNumber) throws CardNotFoundException {
        var visaCard = elcardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        return CardRefillAndChargeResponseDto.builder()
                .balance(visaCard.getBalance())
                .build();
    }

}
