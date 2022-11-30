package top.endant.usercontrolbasedonspring.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import top.endant.usercontrolbasedonspring.config.PassToken;
import top.endant.usercontrolbasedonspring.config.UserLoginToken;
import top.endant.usercontrolbasedonspring.config.requestBodyConfig.RequestWrapper;
import top.endant.usercontrolbasedonspring.mapper.UserMapper;
import top.endant.usercontrolbasedonspring.util.TokenUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 目标方法执行前
     * 该方法在控制器处理请求方法前执行，其返回值表示是否中断后续操作
     * 返回 true 表示继续向下执行，返回 false 表示中断后续操作
     */
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        Method method = handlerMethod.getMethod();

        //检查方法是否有passToken注解，有则跳过认证，直接通过
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("登录失效，请重新登录");
                }
                // 获取 token 中的 user id
                String username;
                try {
                    username = JWT.decode(token).getClaim("username").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("请不要通过非法手段创建token尝试登录");
                }
                //查询数据库，看看是否存在此用户，方法要自己写
                Integer count = userMapper.Test(username);
                if (count == 0) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }

                // 验证 token
                if (TokenUtils.verify(token)) {
                    //获取controller方法名
                    String mName = method.getName();

                    //验证登录操作
                    if ("LoginWithToken".equals(mName)) {
                        return true;
                    }

                    //验证权限
                    Integer authority = userMapper.CheckAuthority(username);
                    if (authority == 0) {//超级管理员
                        return true;
                    } else if (authority == 1) {
                        if ("Update".equals(mName)) {
                            return true;
                        }
                        if ("SelectById".equals(mName)) {
                            return true;
                        }
                        if ("Insert".equals(mName)) {
                            return true;
                        }
                        //对于管理员，进行权限校验
                    } else if (authority == 2) {
                        //对于普通用户，分别进行权限校验，判断是不是本人操作
                        if ("Update".equals(mName)) {
                            // 获取json字符串
                            String jsonParam = new RequestWrapper(request).getBodyString();
                            JSONObject jsonObject = JSON.parseObject(jsonParam);
                            String user = jsonObject.get("username").toString();
                            if (username.equals(user)) {
                                return true;
                            }
                        }
                        if ("SelectById".equals(mName)) {
                            Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                            String id = pathVariables.get("id").toString();
                            String user = userMapper.selectUsernameById(Integer.parseInt(id));
                            if (username.equals(user)) {
                                return true;
                            }
                        }
                    }
                    throw new RuntimeException("您没有此权限");
                } else {
                    throw new RuntimeException("token过期或不正确，请重新登录");
                }

            }
        }
        throw new RuntimeException("没有权限注解一律不通过");
    }

    /**
     * 目标方法执行后
     * 该方法在控制器处理请求方法调用之后、解析视图之前执行
     * 可以通过此方法对请求域中的模型和视图做进一步修改
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) {
        //System.out.println("postHandle执行{}");

    }

    /**
     * 页面渲染后
     * 该方法在视图渲染结束后执行
     * 可以通过此方法实现资源清理、记录日志信息等工作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) {
        //System.out.println("afterCompletion执行异常");

    }
}