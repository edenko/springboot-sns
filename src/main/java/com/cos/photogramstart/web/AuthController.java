package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor // final 필드를 DI할 때 사용
@Controller // 1.IoC 2.파일을 리턴
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);

  private final AuthService authService;

  // 생성자 -> DI 의존성 주입
//  public AuthController(AuthService authService) {
//    this.authService = authService;
//  }

  @GetMapping("/auth/signin")
  public String signinForm() {
    return "auth/signin";
  }

  @GetMapping("/auth/signup")
  public String signupForm() {
    return "auth/signup";
  }

  @PostMapping("/auth/signup")
  public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) {
    User user = signupDto.toEntity();
    authService.signup(user);
    return "auth/signin";
  }
}
