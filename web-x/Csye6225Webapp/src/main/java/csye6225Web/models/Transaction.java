package csye6225Web.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Map;


@Entity
@Table(name = "transaction_table")
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private Integer userid;
    private String description;
    private String merchant;
    private String amount;
    private String date;
    private String category;
    private long receiptid;


    public Transaction() {
    }


    public Transaction(Long id, Integer userid, String description, String merchant,
                       String amount, String date, String category) {
        this.id = id;
        this.userid = userid;
        this.merchant = merchant;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
        //this.receipt = receipt;

    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public String getMerchant() {
        return merchant;
    }



    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

}


