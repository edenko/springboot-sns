package com.cos.photogramstart.config.oauth;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Oauth2DetailsService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> userInfo = oAuth2User.getAttributes();
    String username = "facebook_" + userInfo.get("id").toString();
    String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
    String name = (String) userInfo.get("name");
    String email = (String) userInfo.get("email");

    User userEntity = userRepository.findByUsername(username);
    if(userEntity == null) {
      User user = User.builder()
          .username(username)
          .password(password)
          .name(name)
          .email(email)
          .role("ROLE_USER")
          .build();
      // PrincipalDetails에서 oauth 오버로딩으로 구분한다고 했으니까
      return new PrincipalDetails(userRepository.save(user), oAuth2User.getAttributes());
    }else {
      return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
  }

}
