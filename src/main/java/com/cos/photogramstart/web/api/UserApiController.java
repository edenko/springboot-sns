package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeRespDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class UserApiController {

  private final UserService userService;
  private final SubscribeService subscribeService;

  @PutMapping("/api/v1/user/{id}")
  public CMRespDto<?> update(
      @PathVariable int id,
      @Valid UserUpdateDto userUpdateDto, BindingResult bindingResult,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    User userEntity = userService.updateUser(id, userUpdateDto.toEntity());
    principalDetails.setUser(userEntity); // session 재반영
    return new CMRespDto<>(1, "회원 수정 완료", userEntity);
  }

  @GetMapping("/api/v1/user/{pageUserId}/subscribe")
  public ResponseEntity<?> subscribeList(
      @PathVariable int pageUserId,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    List<SubscribeRespDto> subscribeRespDto = subscribeService.subscribeList(
        principalDetails.getUser().getId(), pageUserId
    );
    return new ResponseEntity<>(
        new CMRespDto<>(1, "구독자 정보 리스트 가져오기 성공", subscribeRespDto),
        HttpStatus.OK);
  }

  @PutMapping("/api/v1/user/{principalId}/profileImageUrl")
  public ResponseEntity<?> profileImageUrlUpdate(
      @PathVariable int principalId,
      MultipartFile profileImageFile, // profile.jsp -> Input name="profileImageFile"
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    User userEntity = userService.userProfileImageUpdate(principalId, profileImageFile);
    principalDetails.setUser(userEntity);
    return new ResponseEntity<>(
        new CMRespDto<>(1, "프로필 사진 변경 성공", null),
        HttpStatus.OK);
  }

}
