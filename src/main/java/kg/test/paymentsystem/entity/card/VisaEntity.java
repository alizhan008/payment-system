package kg.test.paymentsystem.entity.card;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "visa")
public class VisaEntity extends CardEntity {

    public VisaEntity(CardEntity card){
        this.setCardNumber(card.getCardNumber());
        this.setBankName(card.getBankName());
        this.setType(card.getType());
        this.setIssueDate(card.getIssueDate());
    }

}
