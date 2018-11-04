package csye6225Web.daos;

import csye6225Web.models.Transaction;

import java.util.Collection;

public interface TransactionDao {
    Collection<Transaction> findAllTransactions();

    Collection<Transaction> findAllTransactionsByUserid(String user_id);

    Transaction findAllTransactionById(String id);

    void createTransaction(Transaction transaction);

    int updateTransaction(String id, Transaction transaction);

    int deleteTransactiion(String id);
}
