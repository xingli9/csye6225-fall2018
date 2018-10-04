package csye6225Web.bean;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userid;

    //@Column(name = "username")
    private String username;

    //@Column(name = "password")
    private String password;

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer userid) {
        userid = userid;
    }

    public String getUserName() {
        return username;
    }


    public String getPassword() {
        return password;
    }




    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }


}
