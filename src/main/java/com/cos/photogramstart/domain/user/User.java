package com.cos.photogramstart.domain.user;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA : Java Persistance Api -> Java로 데이터를 영구적으로 DB에 저장할 수 있는 Api
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 디비에 테이블 생성
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 디비 전략을 따라감
  private int id;

  @Column(length = 20, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  private String website;

  private String bio;

  @Column(nullable = false)
  private String email;

  private String phone;

  private String gender;

  private String profileImageUrl;

  private String role;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // Image @ManyToOne 이름
  @JsonIgnoreProperties({"user"}) // 호출할 때 image 내부의 "user"는 무시하고 해줘
  private List<Image> images; // 양방향 매핑

  private LocalDateTime createDate;

  @PrePersist // DB에 insert 되기 직전에 실행 -> createDate는 입력하지 않아도 됨
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", name='" + name + '\'' +
        ", website='" + website + '\'' +
        ", bio='" + bio + '\'' +
        ", email='" + email + '\'' +
        ", phone='" + phone + '\'' +
        ", gender='" + gender + '\'' +
        ", profileImageUrl='" + profileImageUrl + '\'' +
        ", role='" + role + '\'' +
        ", createDate=" + createDate +
        '}';
  }
}

