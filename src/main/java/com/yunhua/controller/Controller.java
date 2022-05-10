package com.yunhua.controller;

//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PostAuthorize;

import com.yunhua.golbalexception.exception.BusinessException;
import com.yunhua.golbalexception.vo.ResultEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version V1.0
 * @program: CHK
 * @description:
 * @author: Mr.Zhang
 * @create: 2022-05-07 14:38
 **/
@RestController
public class Controller {


    @GetMapping("index")
//    @PreAuthorize("hasAnyAuthority('test')")
    public String index() {
        return "index!!!";
    }

    @GetMapping("admin")
    public String admin() {
        return "admin!!!";
    }

    @GetMapping("user")
    public String user() {
        return "user!!!";
    }

    @GetMapping("customer")
    public String customer() {
        return "customer!!!";
    }

//    /**
//     * 方法执行前鉴权
//     *
//     * @return
//     */
//    @GetMapping("roleAdmin")
//    @Secured("ROLE_ADMIN")
//    public String roleAdmin() {
//        return "roleAdmin!!!";
//    }
//
//    /**
//     * 方法执行前鉴权
//     *
//     * @return
//     */
//    @GetMapping("preAuthorize")
//    @PostAuthorize("hasAnyRole('ROLE_ADMIN')")
//    public String preAuthorize() {
//        System.out.println("preAuthorize…………");
//        return "preAuthorize!!!";
//    }
//
//    /**
//     * 方法执行完再鉴权
//     *
//     * @return
//     */
//    @GetMapping("postAuthorize")
//    @PostAuthorize("hasAnyRole('ROLE_USER')")
//    public String postAuthorize() {
//        System.out.println("postAuthorize…………");
//        return "PostAuthorize!!!";
//    }
}