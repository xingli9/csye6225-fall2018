package csye6225Web.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
public class User {
    private String username;
    private String password;
//    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
//    private List<Role> roles;
    private boolean active;
    //@OneToMany
    @Id
    @GeneratedValue
    private String userid;
    //@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
   // @Column(nullable = true)
    //private List<Transaction> transactions;

    public User() {

    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public User(String username, String password, boolean active, String userid) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.userid = userid;
    }

//    public List<Transaction> getTransactions() {
//        return transactions;
//    }
//
//    public void setTransactions(List<Transaction> transactions) {
//        this.transactions = transactions;
//    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


//    public List<Role> getRoles() {
//        return new ArrayList<Role>();
//
//    }
}
