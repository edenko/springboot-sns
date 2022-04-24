package com.cos.photogramstart.web.dto.auth;

import com.cos.photogramstart.domain.user.User;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data // Getter, Setter
public class SignupDto {

  @Size(min=2, max=20)
  @NotBlank
  private String username;

  @NotBlank
  private String password;

  @NotBlank
  private String email;

  @NotBlank
  private String name;

  public User toEntity() {
    return User.builder()
        .username(username)
        .password(password)
        .email(email)
        .name(name)
        .build();
  }
}
