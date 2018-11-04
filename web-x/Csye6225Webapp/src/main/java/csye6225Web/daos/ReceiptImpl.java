package csye6225Web.daos;

import csye6225Web.MyConnection;
import csye6225Web.models.Receipt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ReceiptImpl implements ReceiptDao{
    private static ReceiptImpl instance = null;

    private ReceiptImpl() {
    }

    public static ReceiptImpl getInstance() {
        if (instance == null)
            instance = new ReceiptImpl();
        return instance;
    }


    String FIND_ALL_TRANSACTIONS = "SELECT * FROM transaction_table";
    String FIND_ALL_RECEIPTS = "SELECT * FROM receipts WHERE transaction_id=?";
    String CREATE_TRANSACTION = "INSERT INTO transaction_table(id, description, merchant, amount, date, category) VALUES(?,?,?,?,?,?)";
    String CREATE_RECEIPT = "INSERT INTO receipts(id, transaction_id, url) VALUES(?,?,?)";
    String DELETE_RECEIPT = "DELETE FROM receipts WHERE id = ?";
    String UPDATE_RECEIPT = "UPDATE receipts SET url = ?, transaction_id = ? WHERE id = ?";

    @Override
    public void insertReceipt(String transaction_id, Receipt receipt) {

        Connection connection = null;
        PreparedStatement preStatement = null;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(CREATE_RECEIPT);
            preStatement.setString(1, receipt.getId());
            preStatement.setString(2, transaction_id);
            preStatement.setString(3, receipt.getUrl());
            preStatement.executeUpdate();

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
    public int deleteReceipt(String receipt_id) {
        Connection connection = null;
        PreparedStatement preStatement = null;
        int result = 0;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(DELETE_RECEIPT);
            preStatement.setString(1, receipt_id);
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

    @Override
    public int updateReceipt(Receipt receipt) {
        Connection connection = null;
        PreparedStatement preStatement = null;
        int result = 0;
        try {
            connection = MyConnection.getConnection();
            preStatement = connection.prepareStatement(UPDATE_RECEIPT);
            preStatement.setString(1, receipt.getUrl());
            preStatement.setString(2, receipt.getTransaction().getId());
            preStatement.setString(3, receipt.getId());
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
