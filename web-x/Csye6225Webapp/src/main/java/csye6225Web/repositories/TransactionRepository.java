package csye6225Web.repositories;

import csye6225Web.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long>{
}
