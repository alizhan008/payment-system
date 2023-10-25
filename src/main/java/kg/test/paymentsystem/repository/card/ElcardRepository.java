package kg.test.paymentsystem.repository.card;

import kg.test.paymentsystem.entity.card.ElcardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ElcardRepository extends JpaRepository<ElcardEntity,Long> {
    Optional<ElcardEntity> findByCardNumber(Long cardNumber);

}
