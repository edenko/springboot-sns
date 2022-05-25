package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.likes.LikesReposiroty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikesService {

  private final LikesReposiroty likesReposiroty;

  @Transactional
  public void likes(int imageId, int principalId) {
    likesReposiroty.mLikes(imageId, principalId);
  }

  @Transactional
  public void unLikes(int imageId, int principalId) {
    likesReposiroty.mUnLikes(imageId, principalId);
  }

}
