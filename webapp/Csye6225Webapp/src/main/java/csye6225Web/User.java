package csye6225Web;


import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userName;

    private String password;


    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }


    public String getPassword() {
        return password;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
