package csye6225Web.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="receipts")
public class Receipt {

    @Id
    @GeneratedValue
    private String id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id",referencedColumnName = "id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("transaction_id")
    private Transaction transaction;

    private String url;


    public Receipt() {

    }

    public Receipt(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public Receipt(String id, Transaction transaction, String url){
        this.transaction=transaction;
        this.url=url;
        this.id=id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
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
