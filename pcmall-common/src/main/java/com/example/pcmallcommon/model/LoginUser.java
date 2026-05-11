package com.example.pcmallcommon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({"accountNonExpired", "accountNonLocked", "authorities", "username", "password", "credentialsNonExpired", "enabled"})
public class LoginUser implements UserDetails {
    private User user;
    private List<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
        List<GrantedAuthority> list = new ArrayList<>();
        for (String role : roles) {
            list.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }

}
