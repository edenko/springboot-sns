package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.CommentService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.comment.CommentDto;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
  private final CommentService commentService;

  @PostMapping("/api/v1/comment")
  public ResponseEntity<?> commentSave(
      @Valid
      @RequestBody CommentDto commentDto,
      BindingResult bindingResult,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) {
    if(bindingResult.hasErrors()) {
      Map<String, String> errorMap = new HashMap<>();
      for(FieldError error:bindingResult.getFieldErrors()) {
        errorMap.put(error.getField(), error.getDefaultMessage());
      }
      throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
    }else {
      Comment commentEntity = commentService.commentSave(commentDto.getContent(), commentDto.getImageId(), principalDetails.getUser().getId());
      return new ResponseEntity<>(
          new CMRespDto<>(1, "댓글쓰기 성공", commentEntity),
          HttpStatus.CREATED);
    }
  }

  @DeleteMapping("/api/v1/comment/{id}")
  public ResponseEntity<?> commentDelete(@PathVariable int id) {
    commentService.commentDelete(id);
    return new ResponseEntity<>(
        new CMRespDto<>(1, "댓글삭제 성공", null),
        HttpStatus.OK);
  }
}
