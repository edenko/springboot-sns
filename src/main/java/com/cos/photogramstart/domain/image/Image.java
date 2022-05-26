package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
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

  @JsonIgnoreProperties({"images"})
  @JoinColumn(name = "userId") // FK naming
  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  @JsonIgnoreProperties({"image"})
  @OneToMany(mappedBy = "image") // Likes 안에 private Image 'image' 변수이름
  private List<Likes> likes;

  @Transient // 컬럼이 만들어지지 않음
  private boolean likeState;

  @Transient
  private int likeCount;

  // 댓글 (양방향 매핑)
  @OrderBy("id DESC")
  @JsonIgnoreProperties({"image"})
  @OneToMany(mappedBy = "image")
  private List<Comment> comments;

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
