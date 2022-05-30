package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

  private static final long serialVersionUID = 1L;

  private User user;
  private Map<String, Object> attributes;

  public PrincipalDetails(User user) {
    this.user = user;
  }

  public PrincipalDetails(User user, Map<String, Object> attributes) {
    this.user = user;
  }

  @Override
  public <A> A getAttribute(String name) {
    return OAuth2User.super.getAttribute(name);
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes; // {id:12j3123123123, name:eden, email:eden@eden.com}
  }

  // 권한 : 한개가 아닐 수 있음 -> Collection
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collector = new ArrayList<>();
    collector.add(() -> user.getRole());
    return collector;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
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

  @Override
  public String getName() {
    return attributes.get("name").toString();
  }
}
