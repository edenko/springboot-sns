package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Transactional
  public User updateUser(int id, User user) {
    // 1. 영속화
    // 무조건 찾았다(get), 못찾았어 익섹셥(orElseThrow), (orElse)
//    User userEntity = userRepository.findById(id).get();
    User userEntity = userRepository.findById(id).orElseThrow(()->{
        return new CustomValidationApiException("찾을 수 없는 아이디입니다.");
    });
    // 2. 영속화된 object 수정 -> (트랜잭션이 끝나는 시점에) 더티체킹(업데이트됨, repository.save 필요x)
    userEntity.setName(user.getName());
    String rawPassword = user.getPassword();
    String encPassword = bCryptPasswordEncoder.encode(rawPassword);
    userEntity.setPassword(encPassword);
    userEntity.setBio(user.getBio());
    userEntity.setWebsite(user.getWebsite());
    userEntity.setPhone(user.getPhone());
    userEntity.setGender(user.getGender());
    return userEntity;
  }
}
