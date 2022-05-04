package com.cos.photogramstart.web.dto.subscribe;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeRespDto {
  private int id;
  private String username;
  private String profileImageUrl;
  private BigInteger subscribeState; // mariadb는 Integer로 안하면 true를 못받음
  private BigInteger equalUserState; // mariadb는 Integer, mysql은 BigInteger
}
