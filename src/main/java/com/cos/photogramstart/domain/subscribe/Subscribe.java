package com.cos.photogramstart.domain.subscribe;

import com.cos.photogramstart.domain.user.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
  name = "subscribe",
  uniqueConstraints = {
    @UniqueConstraint(
      name="subscribe_uk", // 제약 조건 이름
      columnNames = {"fromUserId", "toUserId"} // 실제 디비 컬럼명 (JoinColumn)
    )
  }
)
public class Subscribe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @JoinColumn(name = "fromUserId")
  @ManyToOne
  private User fromUser;

  @JoinColumn(name = "toUserId")
  @ManyToOne
  private User toUser;

  private LocalDateTime createDate;

  @PrePersist
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }
}
