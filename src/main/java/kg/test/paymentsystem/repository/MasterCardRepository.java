package kg.test.paymentsystem.repository;

import kg.test.paymentsystem.entity.card.MasterCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterCardRepository extends JpaRepository<MasterCard,Long> {
}
