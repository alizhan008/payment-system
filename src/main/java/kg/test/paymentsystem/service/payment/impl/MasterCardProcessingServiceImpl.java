package kg.test.paymentsystem.service.payment.impl;

import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeResponseDto;
import kg.test.paymentsystem.entity.TransactionEntity;
import kg.test.paymentsystem.entity.card.CardEntity;
import kg.test.paymentsystem.entity.card.MasterCardEntity;
import kg.test.paymentsystem.exceptions.CardNotFoundException;
import kg.test.paymentsystem.exceptions.InsufficientBalanceException;
import kg.test.paymentsystem.repository.card.MasterCardRepository;
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
import java.util.*;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MasterCardProcessingServiceImpl implements ProcessingService {

    private final MasterCardRepository masterCardRepository;
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
        for (int i = 0; i < 11; i++) {
            uniqueNumber.append(digits.get(i));
        }
        return Long.parseLong(uniqueNumber.toString());
    }

    @Override
    public CardIssueResponseDto cardIssue(CardEntity card, String userEmail) {
        var masterCard = new MasterCardEntity(card);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден!"));

        masterCard.setIssueDate(LocalDate.now());
        masterCard.setCardNumber(generateUnique16DigitNumber());
        masterCard.setBalance(AMOUNT);
        masterCard.setUser(user);

        masterCardRepository.save(masterCard);
        user.getMasterCards().add(masterCard);
        userRepository.save(user);
        log.info("Карта: {} успешно выпущена!", masterCard.getCardNumber());

        return CardIssueResponseDto.builder()
                .bankName(masterCard.getBankName())
                .type(masterCard.getType())
                .cardNumber(masterCard.getCardNumber())
                .issueDate(masterCard.getIssueDate())
                .build();
    }

    @Override
    public void cardRefill(CardEntity card, String email) throws CardNotFoundException {
        var usr = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));

        var masterCard = masterCardRepository.findByCardNumber(card.getCardNumber())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        if (!usr.getMasterCards().contains(masterCard)) {
            throw new CardNotFoundException("Карта не принадлежит данному пользователю!");
        }
        BigDecimal currentBalance = masterCard.getBalance();
        BigDecimal refillAmount = card.getBalance();

        BigDecimal newBalance = currentBalance.add(refillAmount);
        masterCard.setBalance(newBalance);

        masterCardRepository.save(masterCard);
        var transactionCheck = TransactionEntity.builder()
                .amountRefill(refillAmount)
                .bankName(masterCard.getBankName())
                .cardNumber(masterCard.getCardNumber())
                .userEmail(usr.getEmail())
                .build();
        transactionRepository.save(transactionCheck);
        log.info("Баланс карты: {} успешно пополнен на сумму {}", masterCard.getCardNumber(), refillAmount);
    }

    @Override
    public void chargeTheCard(CardEntity card, String email) throws InsufficientBalanceException, CardNotFoundException {
        var usr = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));
        var masterCard = masterCardRepository.findByCardNumber(card.getCardNumber())
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        if (!usr.getMasterCards().contains(masterCard)) {
            throw new CardNotFoundException("Карта не принадлежит данному пользователю!");
        }
        BigDecimal currentBalance = masterCard.getBalance();

        if (currentBalance.compareTo(card.getBalance()) < 0) {
            throw new InsufficientBalanceException("Недостаточно средств на карте!");
        }
        BigDecimal newBalance = currentBalance.subtract(card.getBalance());
        masterCard.setBalance(newBalance);

        masterCardRepository.save(masterCard);
        var transactionCheck = TransactionEntity.builder()
                .amountCharge(card.getBalance())
                .bankName(masterCard.getBankName())
                .cardNumber(masterCard.getCardNumber())
                .build();
        transactionRepository.save(transactionCheck);
        log.info("Списание с карты: {} на сумму: {} выполнено успешно!", card.getCardNumber(), card.getBalance());
    }

    @Override
    public CardRefillAndChargeResponseDto checkCardBalance(Long cardNumber) throws CardNotFoundException {
        var masterCard = masterCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CardNotFoundException("Карта не найдена!"));

        return CardRefillAndChargeResponseDto.builder()
                .balance(masterCard.getBalance())
                .build();
    }

}
