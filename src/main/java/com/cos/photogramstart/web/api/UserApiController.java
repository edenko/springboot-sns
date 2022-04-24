package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {

  private final UserService userService;

  @PutMapping("/api/v1/user/{id}")
  public CMRespDto<?> update(
      @PathVariable int id,
      @Valid UserUpdateDto userUpdateDto, BindingResult bindingResult,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    if(bindingResult.hasErrors()) {
      Map<String, String> errorMap = new HashMap<>();
      for(FieldError error:bindingResult.getFieldErrors()) {
        errorMap.put(error.getField(), error.getDefaultMessage());
      }
      throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
    }else {
      User userEntity = userService.updateUser(id, userUpdateDto.toEntity());
      principalDetails.setUser(userEntity); // session 재반영
      return new CMRespDto<>(1, "회원 수정 완료", userEntity);
    }
  }
}
