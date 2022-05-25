package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.web.dto.CMRespDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

  private final ImageService imageService;
  private final LikesService likesService;

  @GetMapping("/api/v1/image")
  public ResponseEntity<?> imageStory(
      @AuthenticationPrincipal PrincipalDetails principalDetails,
      @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    Page<Image> imageEntity = imageService.imageList(principalDetails.getUser().getId(), pageable);
    return new ResponseEntity<>(
        new CMRespDto<>(1, "포토리스트 불러오기 성공", imageEntity), // image get -> 무한참조
        HttpStatus.OK);
  }

  @PostMapping("/api/v1/image/{imageId}/likes")
  public ResponseEntity<?> likes(
      @PathVariable int imageId,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    likesService.likes(imageId, principalDetails.getUser().getId());
    return new ResponseEntity<>(
        new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED
    ); // insert -> code 201
  }

  @DeleteMapping("/api/v1/image/{imageId}/likes")
  public ResponseEntity<?> unLikes(
      @PathVariable int imageId,
      @AuthenticationPrincipal PrincipalDetails principalDetails
      ) {
    likesService.unLikes(imageId, principalDetails.getUser().getId());
    return new ResponseEntity<>(
        new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK
    );
  }

}
