package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeRespDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubscribeService {

  private final SubscribeRepository subscribeRepository;
  private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어진 구현체

  @Transactional
  public void subscribe(int fromUserId, int toUserId) {
    try {
      subscribeRepository.mSubscribe(fromUserId, toUserId);
    }catch (Exception e) {
      throw new CustomApiException("이미 구독하였습니다.");
    }
  }

  @Transactional
  public void unSubscribe(int fromUserId, int toUserId) {
    subscribeRepository.mUnSubscribe(fromUserId, toUserId);
  }

  @Transactional(readOnly = true)
  public List<SubscribeRespDto> subscribeList(int principalId, int pageUserId) {
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT u.id, u.username,u.profileImageUrl, ");
    sb.append("IF(( SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id ), 1, 0) subscribeState, ");
    sb.append("IF((? = u.id), 1, 0) equalUserState ");
    sb.append("FROM user u INNER JOIN subscribe s ON s.toUserId = u.id ");
    sb.append("WHERE s.fromUserId = ?"); // 세미콜론 안됨
    Query query = em.createNativeQuery(sb.toString())
        .setParameter(1, principalId)
        .setParameter(2, principalId)
        .setParameter(3, pageUserId);
    JpaResultMapper result = new JpaResultMapper();
    List<SubscribeRespDto> subscribeRespDtos = result.list(query, SubscribeRespDto.class); // 한건: result.uniqueResult()
    return subscribeRespDtos;
  }

}
