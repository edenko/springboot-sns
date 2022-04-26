package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.web.dto.CMRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscribeApiController {

  private final SubscribeService subscribeService;

  @PostMapping("/api/v1/subscribe/{toUserId}")
  public ResponseEntity<?> subscribe(
      @PathVariable int toUserId,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    subscribeService.subscribe(principalDetails.getUser().getId(), toUserId);
    return new ResponseEntity<>(new CMRespDto<>(1, "구독", null), HttpStatus.OK);
  }

  @PostMapping("/api/v1/unSubscribe/{toUserId}")
  public ResponseEntity<?> unSubscribe(
      @PathVariable int toUserId,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    subscribeService.unSubscribe(principalDetails.getUser().getId(), toUserId);
    return new ResponseEntity<>(new CMRespDto<>(1, "구독취소", null), HttpStatus.OK);
  }

}
