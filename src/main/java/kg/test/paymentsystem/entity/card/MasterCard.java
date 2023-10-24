package kg.test.paymentsystem.entity.card;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "master_card")
public class MasterCard extends Card implements Serializable {
    public MasterCard(Card card){
        this.setCardNumber(card.getCardNumber());
        this.setBankName(card.getBankName());
        this.setType(card.getType());
        this.setIssueDate(card.getIssueDate());
    }
}
