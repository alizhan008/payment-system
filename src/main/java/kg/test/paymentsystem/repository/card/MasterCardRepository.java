package kg.test.paymentsystem.repository.card;

import kg.test.paymentsystem.entity.card.MasterCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MasterCardRepository extends JpaRepository<MasterCardEntity,Long> {
    Optional<MasterCardEntity> findByCardNumber(Long cardNumber);
}
