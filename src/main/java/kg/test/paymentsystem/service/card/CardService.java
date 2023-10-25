package kg.test.paymentsystem.service.card;

import kg.test.paymentsystem.common.SecurityContextUtil;
import kg.test.paymentsystem.dtos.issue.CardIssueRequestDto;
import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeRequestDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeResponseDto;
import kg.test.paymentsystem.entity.card.CardEntity;
import kg.test.paymentsystem.service.payment.impl.ElcardProcessingServiceImpl;
import kg.test.paymentsystem.service.payment.impl.MasterCardProcessingServiceImpl;
import kg.test.paymentsystem.service.payment.ProcessingService;
import kg.test.paymentsystem.service.payment.impl.VisaProcessingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final VisaProcessingServiceImpl visaProcessingCenter;
    private final MasterCardProcessingServiceImpl masterCardProcessingCenter;
    private final ElcardProcessingServiceImpl elcardProcessingCenter;

    private ProcessingService getProcessingService(String cardType) {
        return switch (cardType.toUpperCase()) {
            case "VISA" -> visaProcessingCenter;
            case "MASTERCARD" -> masterCardProcessingCenter;
            case "ELCARD" -> elcardProcessingCenter;
            default -> throw new IllegalArgumentException("Не соответствующий тип карты!");
        };
    }

    public CardIssueResponseDto issueCard(CardIssueRequestDto cardRequestDto) {
        CardEntity card = new CardEntity(cardRequestDto);
        ProcessingService paymentSystem = getProcessingService(card.getType());
        return paymentSystem.cardIssue(card, SecurityContextUtil.getCurrentUser());
    }

    public void cardRefill(CardRefillAndChargeRequestDto cardRefillDto) {
        CardEntity card = new CardEntity(cardRefillDto);
        ProcessingService paymentSystem = getProcessingService(card.getType());
        paymentSystem.cardRefill(card, SecurityContextUtil.getCurrentUser());
    }

    public void chargeTheCard(CardRefillAndChargeRequestDto chargeRequestDto) {
        CardEntity card = new CardEntity(chargeRequestDto);
        ProcessingService paymentSystem = getProcessingService(card.getType());
        paymentSystem.chargeTheCard(card, SecurityContextUtil.getCurrentUser());
    }

    public CardRefillAndChargeResponseDto checkCardBalance(String type, Long cardNumber) {
        ProcessingService paymentSystem = getProcessingService(type);
        return paymentSystem.checkCardBalance(cardNumber);
    }

}
