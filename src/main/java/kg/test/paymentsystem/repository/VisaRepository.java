package kg.test.paymentsystem.repository;

import kg.test.paymentsystem.entity.card.Visa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisaRepository extends JpaRepository<Visa,Long> {
}
