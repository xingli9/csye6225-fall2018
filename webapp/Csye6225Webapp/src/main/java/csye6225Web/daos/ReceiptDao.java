package csye6225Web.daos;

import csye6225Web.models.Receipt;

public interface ReceiptDao {

    void insertReceipt(String transaction_id, Receipt receipt);

    int deleteReceipt(String receipt_id);

    int updateReceipt(Receipt receipt);
}
