package top.endant.usercontrolbasedonspring.enity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLogin {
    private String username;
    private String password;
}
