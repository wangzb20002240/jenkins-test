package top.endant.usercontrolbasedonspring.mapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.endant.usercontrolbasedonspring.enity.User;
import top.endant.usercontrolbasedonspring.enity.UserChara;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /*更新用户权限*/
    @Update("update user set  authority = #{authority} where id = #{id}")
    public Integer updateAuthority(UserChara userChara);

    /*username查询权限*/
    @Select("select authority from  user where username = #{username}")
    public Integer CheckAuthority(String username);

    @Select("select count(*) from user where username=#{username} and password=#{password}")
    public Integer Login(String username, String password);

    @Select("select name from user where username=#{username}")
    public String LoginReturnName(String username);

    @Select("select username from user where id=#{id}")
    public String selectUsernameById(Integer id);

    //测试用
    @Select("select COUNT(*) from user where username=#{username}")
    public Integer Test(String username);

    @Select("select count(*) from user")
    public  Integer selectCounts();

    @Select("select count(*) from user where username regexp #{username}")
    public  Integer selectCountsIfUsername(String username);

    @Select("select id, username, name, authority from user")
    public List<UserChara> selectAccountChara();

    @Select("select id, username, name, authority from user limit #{skipCount},#{pageSize}")
    public List<UserChara> selectAcChara(@Param("skipCount")Integer skipCount,@Param("pageSize")Integer pageSize);

    /*id查询用户权限表*/
    @Select("select id, username, name, authority  from user where id = #{id}")
    public UserChara selectAuthorityById(Integer id);

    @Select("select id,username,name,sex,birthday,mail_box,phone,address from user limit #{skipCount},#{pageSize}")
    public List<User> selectAcInfo(@Param("skipCount")Integer skipCount,@Param("pageSize")Integer pageSize);


    @Select("select id,username,name,sex,birthday,mail_box,phone,address from user where username regexp(#{username}) limit #{skipCount},#{pageSize}")
    public List<User> selectAcInfoIfUsername(@Param("skipCount")Integer skipCount,@Param("pageSize")Integer pageSize,@Param("username")String username);

    @Select("select id, username, name, authority from user where username regexp(#{username}) limit #{skipCount},#{pageSize}")
    public List<UserChara> selectAcCharaIfUsername(@Param("skipCount")Integer skipCount,@Param("pageSize")Integer pageSize,@Param("username")String username);


}
