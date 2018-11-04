package csye6225Web.repositories;

        import csye6225Web.models.Transaction;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.repository.CrudRepository;
        import org.springframework.stereotype.Repository;

        import java.util.Optional;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {
    Optional<Transaction> findById(String id);
}
