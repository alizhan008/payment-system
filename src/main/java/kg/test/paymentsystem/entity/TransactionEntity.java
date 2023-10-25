package kg.test.paymentsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number")
    private Long cardNumber;

    @Column(name = "amount_charge")
    private BigDecimal amountCharge;

    @Column(name = "amount_refill")
    private BigDecimal amountRefill;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "user_email")
    private String userEmail;

}


