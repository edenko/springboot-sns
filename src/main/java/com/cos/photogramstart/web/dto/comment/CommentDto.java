package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {
  // NotNull : Null 체크
  // NotEmpty : 빈값, Null 체크
  // NotBlank : 빈값, Null, 공백 체크

  @NotBlank
  private String content;

  @NotNull
  private Integer imageId;

}
