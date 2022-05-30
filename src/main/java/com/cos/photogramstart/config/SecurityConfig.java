package com.cos.photogramstart.config;

import com.cos.photogramstart.config.oauth.Oauth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity // 해당 파일로 시큐리티 활성화
@Configuration // IoC
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final Oauth2DetailsService oauth2DetailsService;

  @Bean
  public BCryptPasswordEncoder encode() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    super 삭제 : 기존 시큐리티가 가진 기능 비활성화
//    super.configure(http);
    http.csrf().disable();
    http.authorizeRequests()
        .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**")
        .authenticated()
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginPage("/auth/signin") // Get
        .loginProcessingUrl("/auth/signin") // Post -> 스프링 시큐리티가 로그인 프로세스 진행
        .defaultSuccessUrl("/")
        .and()
        .oauth2Login() // form login + oauth2 login
        .userInfoEndpoint() // oauth2 로그인하면 최종응답으로 회원정보를 바로 받음
        .userService(oauth2DetailsService);
  }
}
