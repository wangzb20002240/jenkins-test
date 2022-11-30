package top.endant.usercontrolbasedonspring.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.endant.usercontrolbasedonspring.enity.User;
import top.endant.usercontrolbasedonspring.enity.UserChara;
import top.endant.usercontrolbasedonspring.enity.UserLogin;
import top.endant.usercontrolbasedonspring.mapper.UserMapper;
import top.endant.usercontrolbasedonspring.service.IUserService;
import top.endant.usercontrolbasedonspring.util.TokenUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectAcInfo(Integer currentPage, Integer pageSize, String username) {
        Integer skipCount = (currentPage - 1) * pageSize;
        return (username == null
                ? userMapper.selectAcInfo(skipCount, pageSize)
                : userMapper.selectAcInfoIfUsername(skipCount, pageSize, username)
        );
    }

    @Override
    public List<UserChara> selectAcChara(Integer currentPage, Integer pageSize, String username) {
        Integer skipCount = (currentPage - 1) * pageSize;
        return (username == null
                ? userMapper.selectAcChara(skipCount, pageSize)
                : userMapper.selectAcCharaIfUsername(skipCount, pageSize, username)
        );
    }

    @Override
    public Integer selectCounts(String username) {
        return (username == null
                ? userMapper.selectCounts()
                : userMapper.selectCountsIfUsername(username)
        );
    }

    @Override
    public Object Login(UserLogin userLogin) {
        String username = userLogin.getUsername();
        String password = userLogin.getPassword();
        String name = userMapper.LoginReturnName(username);
        String token = TokenUtils.token(username, password);

        Map map = new HashMap<>();
        map.put("name", name);
        map.put("token", token);
        String jsonString = JSON.toJSONString(map);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        return userMapper.Login(username, password) == 1 ? jsonObject : null;
    }

}
