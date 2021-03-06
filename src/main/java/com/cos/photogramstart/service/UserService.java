package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final SubscribeRepository subscribeRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Value("${file.path}") // yml
  private String uploadFolder;

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

  @Transactional(readOnly = true)
  public UserProfileDto profile(int pageUserId, int principalId) {
    UserProfileDto userProfileDto = new UserProfileDto();
    // SELECT * FROM image WHRER userId = :userId;
    User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
      throw new CustomException("해당 프로필은 존재하지 않습니다.");
    });

    userProfileDto.setUser(userEntity);
    userProfileDto.setImageCount(userEntity.getImages().size());
    userProfileDto.setPageOwnerState(pageUserId == principalId);

    int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
    int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
    userProfileDto.setSubscribeState(subscribeState == 1);
    userProfileDto.setSubscribeCount(subscribeCount);

    // 좋아요 카운트 추가
    userEntity.getImages().forEach(image -> {
      image.setLikeCount(image.getLikes().size());
    });

    return userProfileDto;
  }

  @Transactional
  public User userProfileImageUpdate(int principalId, MultipartFile profileImageFile) {
    UUID uuid = UUID.randomUUID();
    String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename(); // 1.jpg
    System.out.println("이미지 파일 이름 : " + imageFileName);

    Path imageFilePath = Paths.get(uploadFolder + imageFileName);

    try {
      Files.write(imageFilePath, profileImageFile.getBytes());
    }catch (Exception e) {
      e.printStackTrace();
    }

    User userEntity = userRepository.findById(principalId).orElseThrow(()->{
      throw new CustomApiException("유저를 찾을 수 없습니다.");
    });
    userEntity.setProfileImageUrl(imageFileName);

    return userEntity;
  }

}
