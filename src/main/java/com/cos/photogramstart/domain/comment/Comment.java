package com.cos.photogramstart.domain.comment;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.persistence.Column;
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
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 100, nullable = false)
  private String content;

  @JsonIgnoreProperties({"images"})
  @JoinColumn(name = "userId")
  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  @JoinColumn(name = "imageId")
  @ManyToOne(fetch = FetchType.EAGER)
  private Image image;

  private LocalDateTime createDate;

  @PrePersist
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }

}
