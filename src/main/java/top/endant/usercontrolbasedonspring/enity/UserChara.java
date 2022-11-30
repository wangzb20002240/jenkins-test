package top.endant.usercontrolbasedonspring.enity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserChara {
    private Integer id;
    private String name;
    private String username;
    private Integer authority;
}
