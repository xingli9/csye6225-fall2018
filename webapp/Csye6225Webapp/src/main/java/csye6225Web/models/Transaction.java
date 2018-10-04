package csye6225Web.models;

import javax.persistence.*;


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



  public Transaction(){}


  public Transaction(Long id, String description, String merchant, String amount, String date, String category)
  {
      this.id=id;
      this.merchant=merchant;
      this.description=description;
      this.amount=amount;
      this.date=date;
      this.category=category;

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


    }

}


