package com.example.go.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class JwtUtils {

    private static long time= 1000*60*60*24*30;//token的有效期30天
    private static String secret="abcdfghiabcdfghiabcdfghiabcdfghi";//32位秘钥

    public static String createToken(int id){
        Date now = new Date();
        Date expiration =  new Date(now.getTime() + time);
        return Jwts.builder()
                //头部信息
                .setHeaderParam("type","JWT")
                //载核
                .setSubject(Integer.toString(id))
                //生成时间
                .setIssuedAt(now)
                //过期时间
                .setExpiration(expiration)
                //加密算法
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    //解析token
    public static Claims getClaimsByToken(String token){
        Claims claims;try {
            claims = Jwts.parser()
                    .setSigningKey(secret) // 设置标识名
                    .parseClaimsJws(token)  //解析token
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return claims;
    }

    //获得id
    public static Integer getId(String token) {
        Claims claimsByToken = JwtUtils.getClaimsByToken(token);
        String bid = claimsByToken.getSubject();
        return Integer.valueOf(bid);
    }
}
