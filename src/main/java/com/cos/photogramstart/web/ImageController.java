package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ImageController {

  private final ImageService imageService;

  @GetMapping({"/", "/image/story"})
  public String story() {
    return "image/story";
  }

  @GetMapping("/image/popular")
  public String popular(Model model) {
    List<Image> images = imageService.popularImage();
    model.addAttribute("images", images);
    return "image/popular";
  }

  @GetMapping("/image/upload")
  public String upload() {
    return "image/upload";
  }

  @PostMapping("/image")
  public String imageUpload(
      ImageUploadDto imageUploadDto,
      @AuthenticationPrincipal PrincipalDetails principalDetails
  ) throws IOException {

    if(imageUploadDto.getFile().isEmpty()) {
      throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
    }
    imageService.imageUpload(imageUploadDto, principalDetails);

    return "redirect:/user/" + principalDetails.getUser().getId();
  }

}
