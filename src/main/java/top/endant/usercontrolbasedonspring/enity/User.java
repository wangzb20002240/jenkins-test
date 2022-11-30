package top.endant.usercontrolbasedonspring.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Integer authority;
    private String name;
    private String sex;
    private LocalDate birthday;
    @TableField("mail_box")
    private String mailBox;
    private String phone;
    private String address;

    public User() {
    }

    public User(Integer id, String username, String password, Integer authority, String name, String sex, LocalDate birthday, String mailBox, String phone, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.mailBox = mailBox;
        this.phone = phone;
        this.address = address;
    }
}
