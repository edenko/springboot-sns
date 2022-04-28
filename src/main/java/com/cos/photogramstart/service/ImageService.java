package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ImageService {

  private final ImageRepository imageRepository;

  @Value("${file.path}") // yml
  private String uploadFolder;

  @Transactional
  public void imageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
    UUID uuid = UUID.randomUUID();
    String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 1.jpg
    System.out.println("이미지 파일 이름 : " + imageFileName);

    Path imageFilePath = Paths.get(uploadFolder + imageFileName);

    // 통신, IO => 예외처리 필요!!!
    try {
      Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
    }catch (Exception e) {
      e.printStackTrace();
    }

    // image db저장
    Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
    Image imageEntity = imageRepository.save(image);
//    System.out.println(imageEntity); // get -> 무한 호출 (user -> image -> user ...)
  }

}
