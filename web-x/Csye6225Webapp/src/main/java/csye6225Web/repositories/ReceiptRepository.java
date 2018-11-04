package csye6225Web.repositories;

import csye6225Web.models.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,String> {
    Optional<Receipt> findById(String id);
    void deleteById(String id);
}
