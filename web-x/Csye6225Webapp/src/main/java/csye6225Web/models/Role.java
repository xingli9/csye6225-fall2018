package csye6225Web.models;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Role extends BaseEntity{

    private String name;
    private String ROLE_;
    private String USER;

    public String Role(String name) {
        this.name = name;
        return name;
    }
}
