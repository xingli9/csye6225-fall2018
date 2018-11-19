package csye6225Web.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Entity
@Table(name = "transaction_table")
public class Transaction {

    @Id
    @GeneratedValue
    private String id;

    //@ManyToOne

    private String user_id;
    private String description;
    private String merchant;
    private String amount;
    private String date;
    private String category;
    @OneToMany(mappedBy = "transaction",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Receipt> attachments=new ArrayList<>();


    public Transaction() {
    }

    public Transaction(String id, String description, String merchant, String amount, String date, String category, String user_id) {
        this.id = id;
        this.description = description;
        this.merchant = merchant;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.user_id = user_id;
    }

    public Transaction(String id, String description, String merchant,
                       String amount, String date, String category, String user_id, ArrayList<Receipt> attachments) {
        this.id = id;
        //this.user = new User();
        this.description = description;
        this.merchant = merchant;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.user_id = user_id;
        this.attachments=attachments;

    }

    public void addReceipt(Receipt receipt)
    {
        this.attachments.add(receipt);
        receipt.setTransaction(this);
    }

    public void removeReceipt(Receipt receipt)
    {
        this.attachments.remove(receipt);
        receipt.setTransaction(null);

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public List<Receipt> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Receipt> attachments) {
        this.attachments = attachments;
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

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public String getMerchant() {
        return merchant;
    }

    public void setId(String id) {
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


    @Override
    public String toString() {
        String rst = "";
        rst += this.id + "  ";
        rst += this.description + "  ";
        rst += this.amount + "  ";
        rst += this.date + "  ";
        rst += this.merchant + "  ";
        rst += this.category + "  ";
        List<Receipt> receipts = this.getAttachments();
        for (Receipt rc:receipts) {
            rst += rc.getId() + "  ";
            rst += rc.getUrl() + "  ";
        }
        return rst;
    }

}


