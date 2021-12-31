package com.sustechstore.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;

import java.util.Date;
import java.util.List;


public class TokenUtils {
    private static final long EXPIRE_TIME= 10*60*60*1000;
    private static final String TOKEN_SECRET="SUSTech";

    public static String sign(int userID){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userID", String.format("%d",userID))
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }


    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("userID: " + jwt.getClaim("userID").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            System.out.println("认证不通过");
            return false;
        }
    }

    public static int getID(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            return Integer.parseInt(jwt.getClaim("userID").asString());
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static int getUserid(HttpHeaders req) {
        int userid;
        List<String> tokenList = req.get("token");
        if(tokenList == null||tokenList.size()==0){
            //Error: request with no token
            userid = -1;
        }
        String token = tokenList.get(0);
        System.out.println(token);
        if(!TokenUtils.verify(token)){
            //Error: invalid token
            userid = -1;
        }
        userid = TokenUtils.getID(token);
        return userid;
    }
}