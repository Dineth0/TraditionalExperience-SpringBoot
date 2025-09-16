package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Payment;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    void deleteByWorkshopRegistrationId(Long id);

    @Query(value = "SELECT p.* FROM payment p " +
            "LEFT JOIN workshop w ON p.workshop_id = w.id " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Payment> findPaymentPaginated(@Param("limit") int limit, @Param("offset") int offset);


    @Query(value = "SELECT COUNT(*) FROM payment ", nativeQuery = true)
    int getTotalPaymentCount();
}
