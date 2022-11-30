package top.endant.usercontrolbasedonspring.service;

import org.springframework.transaction.annotation.Transactional;
import top.endant.usercontrolbasedonspring.enity.User;
import top.endant.usercontrolbasedonspring.enity.UserChara;
import top.endant.usercontrolbasedonspring.enity.UserLogin;

import java.util.List;

@Transactional
public interface IUserService {

    public List<User>  selectAcInfo(Integer currentPage,Integer pageSize, String username);

    public List<UserChara>  selectAcChara(Integer currentPage,Integer pageSize, String username);

    public Integer selectCounts(String username);

    public Object Login(UserLogin userLogin);

}
