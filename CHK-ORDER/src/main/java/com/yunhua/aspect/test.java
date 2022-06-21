package com.yunhua.aspect;

import io.jsonwebtoken.Claims;

import static com.yunhua.utils.JwtUtil.createJWT;
import static com.yunhua.utils.JwtUtil.parseJWT;

public class test {
    public static void main(String[] args) throws Exception {

//        UUID uuid=UUID.randomUUID();
//        String str = uuid.toString();
//        String uuidStr=str.replace("-", "");
//        System.out.println(uuidStr);

        String jwt = createJWT("11");
        System.out.println(jwt);
        Claims claims = parseJWT(jwt);
        String subject = claims.getSubject();
        System.out.println(subject);
        System.out.println(claims);
    }
}
