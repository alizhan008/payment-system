package kg.test.paymentsystem.entity.card;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.UUID)
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

}
