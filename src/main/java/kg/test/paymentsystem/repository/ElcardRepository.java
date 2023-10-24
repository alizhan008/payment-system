package kg.test.paymentsystem.repository;

import kg.test.paymentsystem.entity.card.Elcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElcardRepository extends JpaRepository<Elcard,Long> {
    Elcard findByCardNumber(Long cardNumber);

}
