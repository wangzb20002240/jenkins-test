package top.endant.usercontrolbasedonspring.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用token验证用户是否登录
 */
public class TokenUtils {
    //设置过期时间
    private static final long EXPIRE_DATE = 1000 * 60 * 60 * 24; //一天
    //token秘钥
    private static final String TOKEN_SECRET = "awgfGDLJA-Fdsh.AGSgdfu/fguGuwu2022BQWf";

    public static String token(String username, String password) {

        String token;
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("password", password).withExpiresAt(date)
                    .sign(algorithm);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return token;
    }

    /**
     *  验证token，通过返回true
     *  [token]需要校验的串
     */
    public static boolean verify(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();

            verifier.verify(token);
            return true;
        } catch (Exception e) {
            System.out.println("校验失败");
            return false;
        }
    }
}
