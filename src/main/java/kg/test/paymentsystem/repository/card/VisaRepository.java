package kg.test.paymentsystem.repository.card;

import kg.test.paymentsystem.entity.card.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<VisaEntity,Long> {
   Optional<VisaEntity> findByCardNumber(Long cardNumber);

}
