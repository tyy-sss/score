package com.example.go.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody();
    }
}
