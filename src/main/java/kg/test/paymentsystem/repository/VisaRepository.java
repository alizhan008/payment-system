package kg.test.paymentsystem.repository;

import kg.test.paymentsystem.entity.card.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaRepository extends JpaRepository<Visa,Long> {
    Visa findByCardNumber(Long cardNumber);

}
