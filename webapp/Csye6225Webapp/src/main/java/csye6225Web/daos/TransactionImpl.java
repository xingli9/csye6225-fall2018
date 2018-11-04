package csye6225Web.daos;

import csye6225Web.MyConnection;
import csye6225Web.models.Receipt;
import csye6225Web.models.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionImpl implements TransactionDao {
    private static TransactionImpl instance = null;

    private TransactionImpl() {
    }

    public static TransactionImpl getInstance() {
        if (instance == null)
            instance = new TransactionImpl();
        return instance;
    }

    String FIND_ALL_TRANSACTIONS = "SELECT * FROM transaction_table";
    String FIND_ALL_TRANSACTIONS_BY_USERID = "SELECT * FROM transaction_table WHERE user_id=?";
    String FIND_TRANSACTION_BY_ID = "SELECT * FROM transaction_table WHERE id=?";
    String FIND_ALL_RECEIPTS = "SELECT * FROM receipts WHERE transaction_id=?";
    String CREATE_TRANSACTION = "INSERT INTO transaction_table(id, description, merchant, amount, date, category, user_id) VALUES(?,?,?,?,?,?,?)";
    String CREATE_RECEIPT = "INSERT INTO receipts(id, transaction_id, url) VALUES(?,?,?)";
    String DELETE_TRANSACTION = "DELETE FROM transaction_table WHERE id = ?";

    String UPDATE_TRANSACTION_TABLE = "UPDATE transaction_table SET description = ?, merchant = ?, amount = ?, date = ?, category = ?, user_id = ? WHERE id = ?";

    String CREATE_DEVELOPER = "INSERT INTO developer(id, developer_key) VALUES(?,?)";
    String CREATE_PHONE = "INSERT INTO phone(id, phone, `primary`, person_id) VALUES(?,?,?,?)";


    @Override
    public Collection<Transaction> findAllTransactions() {
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet result = null;
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(FIND_ALL_TRANSACTIONS);
            result = preStatement.executeQuery();
            while (result.next()) {
                String id = result.getString("id");
                String description = result.getString("description");
                String merchant = result.getString("merchant");
                String amount = result.getString("amount");
                String date = result.getString("date");
                String category = result.getString("category");
                String user_id = result.getString("user_id");
                Transaction transaction = new Transaction(id, description, merchant, amount, date, category, user_id);

                Collection<Receipt> receipts = new ArrayList<>();

                preStatement = connection.prepareStatement(FIND_ALL_RECEIPTS);
                preStatement.setString(1, transaction.getId());
                ResultSet result1 = preStatement.executeQuery();
                while (result1.next()) {
                    String receiptId = result.getString("id");
                    String url = result.getString("url");
                    Receipt receipt = new Receipt(receiptId, transaction, url);
                    transaction.addReceipt(receipt);
                }
                transactions.add(transaction);
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return transactions;

    }

    @Override
    public Collection<Transaction> findAllTransactionsByUserid(String user_id) {
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet result = null;
        List<Transaction> transactions = new ArrayList<Transaction>();
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(FIND_ALL_TRANSACTIONS_BY_USERID);
            preStatement.setString(1, user_id);
            result = preStatement.executeQuery();
            while (result.next()) {
                String id = result.getString("id");
                String description = result.getString("description");
                String merchant = result.getString("merchant");
                String amount = result.getString("amount");
                String date = result.getString("date");
                String category = result.getString("category");
                Transaction transaction = new Transaction(id, description, merchant, amount, date, category, user_id);

                Collection<Receipt> receipts = new ArrayList<>();

                preStatement = connection.prepareStatement(FIND_ALL_RECEIPTS);
                preStatement.setString(1, transaction.getId());
                ResultSet result1 = preStatement.executeQuery();
                while (result1.next()) {
                    String receiptId = result1.getString("id");
                    String url = result1.getString("url");
                    Receipt receipt = new Receipt(receiptId, transaction, url);
                    transaction.addReceipt(receipt);
                }
                transactions.add(transaction);
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return transactions;
    }

    @Override
    public Transaction findAllTransactionById(String id) {
        Connection connection = null;
        PreparedStatement preStatement = null;
        ResultSet result = null;
        Transaction transaction = new Transaction();
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(FIND_TRANSACTION_BY_ID);
            preStatement.setString(1, id);
            result = preStatement.executeQuery();

            String description = result.getString("description");
            String merchant = result.getString("merchant");
            String amount = result.getString("amount");
            String date = result.getString("date");
            String category = result.getString("category");
            String user_id = result.getString("user_id");
            transaction = new Transaction(id, description, merchant, amount, date, category, user_id);

            Collection<Receipt> receipts = new ArrayList<>();

            preStatement = connection.prepareStatement(FIND_ALL_RECEIPTS);
            ResultSet result1 = preStatement.executeQuery();
            while (result1.next()) {
                String receiptId = result.getString("id");
                String url = result.getString("url");
                Receipt receipt = new Receipt(receiptId, transaction, url);
                transaction.addReceipt(receipt);
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return transaction;

    }

    @Override
    public void createTransaction(Transaction transaction) {
        Connection connection = null;
        PreparedStatement preStatement = null;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(CREATE_TRANSACTION);
            preStatement.setString(1, transaction.getId());
            preStatement.setString(2, transaction.getDescription());
            preStatement.setString(3, transaction.getMerchant());
            preStatement.setString(4, transaction.getAmount());
            preStatement.setString(5, transaction.getDate());
            preStatement.setString(6, transaction.getCategory());
            preStatement.setString(7, transaction.getUser_id());
            preStatement.executeUpdate();

            Collection<Receipt> receipts = transaction.getAttachments();
            if (receipts!= null && !receipts.isEmpty()) {
                for (Receipt r : receipts) {
                    preStatement = connection.prepareStatement(CREATE_RECEIPT);
                    preStatement.setString(1, r.getId());
                    preStatement.setString(2, transaction.getId());
                    preStatement.setString(3, r.getUrl());
                    preStatement.executeUpdate();
                }
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    @Override
    public int updateTransaction(String id, Transaction transaction) {

        Connection connection = null;
        PreparedStatement preStatement = null;
        int result = 0;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(UPDATE_TRANSACTION_TABLE);
            preStatement.setString(1, transaction.getDescription());
            preStatement.setString(2, transaction.getMerchant());
            preStatement.setString(3, transaction.getAmount());
            preStatement.setString(4, transaction.getDate());
            preStatement.setString(5, transaction.getCategory());
            preStatement.setString(6, transaction.getUser_id());
            preStatement.setString(7, id);
            result = preStatement.executeUpdate();

            Collection<Receipt> receipts = transaction.getAttachments();
            if (receipts!= null && !receipts.isEmpty()) {
                for (Receipt r : receipts) {
                    preStatement = connection.prepareStatement(CREATE_RECEIPT);
                    preStatement.setString(1, r.getId());
                    preStatement.setString(2, transaction.getId());
                    preStatement.setString(3, r.getUrl());
                    preStatement.executeUpdate();
                }
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public int deleteTransactiion(String id) {
        Connection connection = null;
        PreparedStatement preStatement = null;
        int result = 0;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(DELETE_TRANSACTION);
            preStatement.setString(1, id);
            result = preStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }
}
