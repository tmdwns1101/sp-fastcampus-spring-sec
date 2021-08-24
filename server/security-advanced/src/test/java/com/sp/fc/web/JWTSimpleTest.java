package com.sp.fc.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JWTSimpleTest {

    private void printToken(String token) {
        String[] tokens = token.split("\\.");
        System.out.println("header: " + new String(Base64.getDecoder().decode(tokens[0])));
        System.out.println("body: " + new String(Base64.getDecoder().decode(tokens[1])));
    }

    @DisplayName("1. jjwt 를 이용한 토큰 테스트")
    @Test
    void test_1(){
//        Map<String, Object> clamins = new HashMap<>();
//        clamins.put("name","seoungjun");
//        clamins.put("price", 3000);
//        String okta_token = Jwts.builder()
//                .addClaims(clamins)
//                .signWith(SignatureAlgorithm.HS256,"tmdwns1101")
//                .compact();
//        System.out.println("okta_token = " + okta_token);
//        printToken(okta_token);
    }


    @DisplayName("2. java-jwt 를 이용한 토큰 테스트")
    @Test
    void test_2() {
        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary("tmdwns1101");

        String sign = JWT.create().withClaim("name", "seungjun").withClaim("price",3000)
                .sign(Algorithm.HMAC256(SEC_KEY)); //java-jwt
        System.out.println("sign = " + sign);
        printToken(sign);

//        Jws<Claims> tokenInfo = Jwts.parser().setSigningKey(SEC_KEY).parseClaimsJws(sign); //jjwt okta
//        System.out.println("tokenInfo = " + tokenInfo);
    }



}
