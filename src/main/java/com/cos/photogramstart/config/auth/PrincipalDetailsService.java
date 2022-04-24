package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service // IoC
public class PrincipalDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  // 1. password는 시큐리티가 알아서 처리!
  // 2. 리턴이 잘 되면 자동으로 UserDetails 타입을 세션을 만든다.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User userEntity = userRepository.findByUsername(username);
    if(userEntity == null) {
      return null;
    }else {
      return new PrincipalDetails(userEntity);
    }
  }
}
