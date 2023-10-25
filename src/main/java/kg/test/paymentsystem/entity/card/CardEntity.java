package kg.test.paymentsystem.entity.card;

import jakarta.persistence.*;
import kg.test.paymentsystem.dtos.issue.CardIssueRequestDto;
import kg.test.paymentsystem.dtos.refill.CardRefillAndChargeRequestDto;
import kg.test.paymentsystem.entity.user.UserEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "type")
    private String type;

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public CardEntity(CardIssueRequestDto cardIssueResponseDto){
        this.setBankName(cardIssueResponseDto.getBankName());
        this.setType(cardIssueResponseDto.getType().toUpperCase());
    }

    public CardEntity(CardRefillAndChargeRequestDto cardRefillDto){
        this.setType(cardRefillDto.getType().toUpperCase());
        this.setCardNumber(cardRefillDto.getCardNumber());
        this.setBalance(cardRefillDto.getBalance());
    }

}
