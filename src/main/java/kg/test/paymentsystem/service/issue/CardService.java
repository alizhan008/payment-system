package kg.test.paymentsystem.service.issue;

import kg.test.paymentsystem.dtos.CardRequestDto;
import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.user.User;
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

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return User.builder()
                .email(userDetails.getUsername())
                .build();
    }

    public void issueCard(CardRequestDto cardRequestDto){
        Card card = Card.builder()
                .bankName(cardRequestDto.getBankName())
                .type(cardRequestDto.getType())
                .build();
        var user = getCurrentUser();
        PaymentSystem paymentSystem = getProcessingCenter(card.getType());
        paymentSystem.cardIssue(card,user);
    }

    public void cardRefill(CardRequestDto cardRequestDto){
        Card card = Card.builder()
                .cardNumber(cardRequestDto.getCardNumber())
                .balance(cardRequestDto.getBalance())
                .build();
        PaymentSystem paymentSystem = getProcessingCenter(card.getType());
        paymentSystem.cardRefill(card);
    }

    public void chargeTheCard(CardRequestDto cardRequestDto){
        Card card = Card.builder()
                .cardNumber(cardRequestDto.getCardNumber())
                .balance(cardRequestDto.getBalance())
                .build();
        PaymentSystem paymentSystem = getProcessingCenter(card.getType());
        paymentSystem.chargeTheCard(card);
    }

}
