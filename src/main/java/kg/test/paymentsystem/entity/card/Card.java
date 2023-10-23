package kg.test.paymentsystem.entity.card;

import jakarta.persistence.*;
import kg.test.paymentsystem.entity.user.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@MappedSuperclass
@Entity
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "type")
    private String type;

    @Column(name = "card_number")
    private Integer cardNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
