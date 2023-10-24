package kg.test.paymentsystem.repository;

import kg.test.paymentsystem.entity.card.MasterCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCard,Long> {
    MasterCard findByCardNumber(Long cardNumber);
}
