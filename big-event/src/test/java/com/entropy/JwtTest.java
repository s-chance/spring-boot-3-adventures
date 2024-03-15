package com.entropy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "black");
        // 生成 jwt
        String token = JWT.create()
                .withClaim("user", claims) // 添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 添加过期时间
                .sign(Algorithm.HMAC256("secret"));// 指定算法，配置密钥

        System.out.println(token);
    }

    @Test
    public void testParse() {
        // 定义字符串，模拟用户传递的 token
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6ImJsYWNrIn0sImV4cCI6MTcwOTkzMDY1Nn0" +
                ".9ztgvOSUI2nmI0gdJ2-rL5pt9-q7GhmW4nVi1ShCpfw";

        // jwt 验证器，需要指定正确的算法和密钥
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("secret")).build();

        // 验证 token，返回解析后的结果
        DecodedJWT decodedJWT = jwtVerifier.verify(token);

        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));
    }
}
