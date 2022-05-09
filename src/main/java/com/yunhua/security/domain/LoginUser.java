package com.yunhua.security.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.yunhua.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @program: CHK
 * @description: 用户登录类
 * @author: Mr.Zhang
 * @create: 2022-05-07 18:07
 **/
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private User user;

    //定义权限列表
    private List<String> permissions;

    //对象不会被序列化的流中
    @JSONField(serialize = false)
    private Set<SimpleGrantedAuthority> authorities;

    public LoginUser(User user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null){
            return authorities;
        }
        //把permissions类型的权限信息封装进SimpleGrantedAuthority对象
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
