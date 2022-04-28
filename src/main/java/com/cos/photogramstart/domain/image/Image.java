package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.user.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "image")
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String caption; // 사진 설명

  private String postImageUrl; // 사진을 서버에 저장 후, 디비에는 저장 경로를 넣음

  @JoinColumn(name = "userId") // FK naming
  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  private LocalDateTime createDate;

  @PrePersist
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }

//  오브젝트를 콘솔에 sout 출력할 때 문제가 될 수 있어서 User 부분을 출력되지 않게 함
//  @Override
//  public String toString() {
//    return "Image{" +
//        "id=" + id +
//        ", caption='" + caption + '\'' +
//        ", postImageUrl='" + postImageUrl + '\'' +
//        ", createDate=" + createDate +
//        '}';
//  }
}
