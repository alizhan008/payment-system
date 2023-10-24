package kg.test.paymentsystem.service.card;

import kg.test.paymentsystem.dtos.issue.CardIssueRequestDto;
import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillRequestDto;
import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.service.paymentcenter.ElcardProcessingCenter;
import kg.test.paymentsystem.service.paymentcenter.MasterCardProcessingCenter;
import kg.test.paymentsystem.service.paymentcenter.PaymentSystem;
import kg.test.paymentsystem.service.paymentcenter.VisaProcessingCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final VisaProcessingCenter visaProcessingCenter;
    private final MasterCardProcessingCenter masterCardProcessingCenter;
    private final ElcardProcessingCenter elcardProcessingCenter;

    private PaymentSystem getProcessingCenter(String cardType){
        return switch (cardType.toLowerCase()) {
            case "visa" -> visaProcessingCenter;
            case "mastercard" -> masterCardProcessingCenter;
            case "elcard" -> elcardProcessingCenter;
            default -> throw new IllegalArgumentException("Не соответствующий тип карты!");
        };
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    public CardIssueResponseDto issueCard(CardIssueRequestDto cardRequestDto){
        Card card = new Card(cardRequestDto);
        var userEmail = getCurrentUser();
        PaymentSystem paymentSystem = getProcessingCenter(card.getType());
        return paymentSystem.cardIssue(card,userEmail);
    }

    public void cardRefill(CardRefillRequestDto cardRefillDto){
        Card card = new Card(cardRefillDto);
        PaymentSystem paymentSystem = getProcessingCenter(card.getType());
        paymentSystem.cardRefill(card);
    }

    public void chargeTheCard(){
        Card card = new Card();
        PaymentSystem paymentSystem = getProcessingCenter(card.getType());
        paymentSystem.chargeTheCard(card);
    }

}
