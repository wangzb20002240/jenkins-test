package top.endant.usercontrolbasedonspring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.endant.usercontrolbasedonspring.mapper.UserMapper;

@SpringBootTest
class UserControlBasedOnSpringApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(userMapper.selectAccountChara());
    }

}
