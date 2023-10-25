package kg.test.paymentsystem.controller.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kg.test.paymentsystem.dtos.issue.CardIssueRequestDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeRequestDto;
import kg.test.paymentsystem.dtos.issue.CardIssueResponseDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeResponseDto;
import kg.test.paymentsystem.service.card.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kg.test.paymentsystem.common.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API+PAY_SYSTEM)
public class PaymentSystemController {

    private final CardService cardService;

    /**
     * Метод для выпуска новой банковской карты.
     *
     * @param cardRequestDto Запрос на выпуск карты, содержащий данные для создания карты.
     * @return HTTP-ответ с данными о выпущенной карте.
     */
    @PostMapping(ISSUE)
    public ResponseEntity<CardIssueResponseDto> cardIssue(@RequestBody @Valid CardIssueRequestDto cardRequestDto) {
        return ResponseEntity.ok(cardService.issueCard(cardRequestDto));
    }

    /**
     * Метод для пополнения баланса банковской карты.
     *
     * @param cardRefillDto Запрос на пополнение карты, содержащий данные о пополнении.
     * @return HTTP-ответ с сообщением об успешном пополнении.
     */
    @PostMapping(REFILL)
    public ResponseEntity<?> cardRefill(@RequestBody CardRefillAndChargeRequestDto cardRefillDto) {
        cardService.cardRefill(cardRefillDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Метод для списания средств с банковской карты.
     *
     * @param chargeRequestDto Запрос на списание, содержащий данные о списании средств.
     * @return HTTP-ответ с сообщением об успешном списании.
     */
    @PostMapping(CHARGE)
    public ResponseEntity<?> chargeTheCard(@RequestBody CardRefillAndChargeRequestDto chargeRequestDto) {
        cardService.chargeTheCard(chargeRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Метод для проверки баланса банковской карты.
     *
     * @param type Тип карты (например, "visa" или "mastercard").
     * @param cardNumber Номер карты для проверки баланса.
     * @return HTTP-ответ с данными о балансе карты.
     */
    @GetMapping(CHECK)
    public ResponseEntity<CardRefillAndChargeResponseDto> checkCardBalance(
            @RequestParam(name = "type") @NotBlank String type,
            @RequestParam(name = "card-number") @NotBlank Long cardNumber){
        return ResponseEntity.ok(cardService.checkCardBalance(type,cardNumber));
    }

}
