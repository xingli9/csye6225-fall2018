package csye6225Web.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;






@Entity
@Table(name="transaction_table")
public class Transaction {

  @Id
  @GeneratedValue
  private Long id;

  private String description;
  private String merchant;
  private String amount;
  private String date;
  private String category;

  @OneToMany(mappedBy = "transaction",cascade = CascadeType.ALL,orphanRemoval = true)
  private List<Receipt> attachments=new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id",referencedColumnName = "id")
  @OnDelete(action= OnDeleteAction.CASCADE)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  @JsonProperty("user_id")
  private User user;


  public Transaction(){}


  public Transaction(Long id, String description, String merchant, String amount, String date, String category,ArrayList<Receipt> attachments)
  {
      this.id=id;
      this.merchant=merchant;
      this.description=description;
      this.amount=amount;
      this.date=date;
      this.category=category;
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

    public void setUser(User user) {
        this.user = user;
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

    public List<Receipt> getAttachments() {
        return attachments;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public void setAttachments(List<Receipt> attachments) {
        this.attachments = attachments;
    }
}




