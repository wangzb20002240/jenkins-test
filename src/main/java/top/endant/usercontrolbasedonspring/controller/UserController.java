package top.endant.usercontrolbasedonspring.controller;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.endant.usercontrolbasedonspring.config.PassToken;
import top.endant.usercontrolbasedonspring.config.UserLoginToken;
import top.endant.usercontrolbasedonspring.enity.User;
import top.endant.usercontrolbasedonspring.enity.UserChara;
import top.endant.usercontrolbasedonspring.enity.UserLogin;
import top.endant.usercontrolbasedonspring.mapper.UserMapper;
import top.endant.usercontrolbasedonspring.service.IUserService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService userService;

    @UserLoginToken
    @GetMapping("/tokenCheck")
    public Result LoginWithToken(@RequestHeader String token){
        String username = JWT.decode(token).getClaim("username").asString();
        String name = userMapper.LoginReturnName(username);
        return (new Result(Code.LOGIN_OK, name, ""));
    }

    /*登录验证返回name和token*/
    @PostMapping("/login")
    public Result Login(@RequestBody UserLogin userLogin) {

        Object login = userService.Login(userLogin);

        String msg = login != null ? "登录成功" : "用户名或密码错误";

        return login != null
                ? new Result(Code.LOGIN_OK, login, msg)
                : new Result(Code.LOGIN_ERR, null, msg);
    }

    @UserLoginToken
    /*获取单个用户具体信息*/
    @GetMapping("/{id}")
    public Result SelectById(@PathVariable Integer id) {
        User user = userMapper.selectById(id);
        String msg = user != null ? "" : "获取id为" + id + "的用户信息失败，请重试！";
        return new Result(user != null ? Code.SELECT_OK : Code.SYSTEM_ERR, user, msg);
    }

    @PassToken
    /*获取用户数量*/
    @GetMapping
    private Result SelectCounts(String username) {
        Integer counts = userService.selectCounts(username);
        String msg = counts != null ? "" : "获取用户数量信息，请重试！";
        return new Result(counts != null ? Code.SELECT_OK : Code.SYSTEM_ERR, counts, msg);
    }

    @PassToken
    /*获取所有账户基本信息*/
    @GetMapping("/info")
    public Result SelectAllAccountInfo() {
        List<User> users = userMapper.selectList(null);
        String msg = users != null ? "" : "获取用户信息失败，请重试！";
        return new Result(users != null ? Code.SELECT_OK : Code.SYSTEM_ERR, users, msg);
    }

    @PassToken
    /*获取当前页账户基本信息*/
    @GetMapping("/info/limit")
    public Result SelectAllAcInfo(Integer currentPage, Integer pageSize, String username) {
        List<User> users = userService.selectAcInfo(currentPage, pageSize, username);
        String msg = users != null ? "" : "获取用户信息失败，请重试！";
        return new Result(users != null ? Code.SELECT_OK : Code.SYSTEM_ERR, users, msg);
    }

    @PassToken
    /*获取所有账户权限信息*/
    @GetMapping("/chara")
    public Result SelectAllAccountChara() {
        List<UserChara> users = userMapper.selectAccountChara();
        String msg = users != null ? "" : "获取用户权限信息失败，请重试！";
        return new Result(users != null ? Code.SELECT_OK : Code.SYSTEM_ERR, users, msg);
    }

    @PassToken
    /*获取当前页账户权限信息*/
    @GetMapping("/chara/limit")
    public Result SelectAllAcChara(Integer currentPage, Integer pageSize, String username) {
        List<UserChara> users = userService.selectAcChara(currentPage, pageSize, username);
        String msg = users != null ? "" : "获取用户权限信息失败，请重试！";
        return new Result(users != null ? Code.SELECT_OK : Code.SYSTEM_ERR, users, msg);
    }

    @UserLoginToken
    /*根据id删除用户*/
    @DeleteMapping("/delete/{id}")
    public Result DeleteById(@PathVariable Integer id) {
        int res = userMapper.deleteById(id);
        String msg = res > 0 ? "" : "删除失败，请重试！";
        return new Result(res > 0 ? Code.DELETE_OK : Code.SYSTEM_ERR, null, msg);
    }

    @UserLoginToken
    /*批量删除用户*/
    @DeleteMapping("/delete")
    public Result DeleteByIds(@RequestBody Integer[] ids) {
        int res = userMapper.deleteBatchIds(Arrays.asList(ids));
        String msg = res == ids.length ? "" : "删除失败，请重试！";
        return new Result(res == ids.length ? Code.DELETE_OK : Code.SYSTEM_ERR, null, msg);
    }

    @UserLoginToken
    /*新增用户*/
    @PostMapping("/insert")
    public Result Insert(@RequestBody User user) {
        int res = userMapper.insert(user);
        String msg = res > 0 ? "" : "新增用户失败，请重试！";
        return new Result(res > 0 ? Code.INSERT_OK : Code.SYSTEM_ERR, null, msg);
    }

    @UserLoginToken
    /*更新用户*/
    @PutMapping("/update")
    public Result Update(@RequestBody User user) {
        int res = userMapper.updateById(user);
        String msg = res > 0 ? "" : "更新用户失败，请重试！";
        return new Result(res > 0 ? Code.UPDATE_OK : Code.SYSTEM_ERR, msg);
    }

    @UserLoginToken
    /*更新用户*/
    @PutMapping("/chara/update")
    public Result UpdateAuthority(@RequestBody UserChara userChara){
        int res = userMapper.updateAuthority(userChara);
        String msg = res > 0 ? "" : "更新用户权限失败，请重试！";
        return new Result(res > 0 ? Code.UPDATE_OK : Code.SYSTEM_ERR, msg);
    }

    @UserLoginToken
    /*获取单个用户权限信息*/
    @GetMapping("/chara/{id}")
    public Result SelectAuthorityById(@PathVariable Integer id) {
        UserChara userChara = userMapper.selectAuthorityById(id);
        String msg = userChara != null ? "" : "获取id为" + id + "的用户权限信息失败，请重试！";
        return new Result(userChara != null ? Code.SELECT_OK : Code.SYSTEM_ERR, userChara, msg);
    }
}
