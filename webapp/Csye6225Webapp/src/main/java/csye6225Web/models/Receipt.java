package csye6225Web.models;


import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;

@Entity
@Table(name="Receipts")
public class Receipt {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id",referencedColumnName = "id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("transaction_id")
    private Transaction transaction;

    private String url;


    public Receipt() {}

    public Receipt( Long id,Transaction transaction, String url){
        this.transaction=transaction;
        this.url=url;
        this.id=id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
