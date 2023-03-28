package com.kh.app.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
  private Long memberId;
  private String email;
  private String passwd;
  private String nickname;
  private String gender;
  private String hobby;
  private String region;
  private byte[] pic;
  private LocalDateTime cmate;
  private LocalDateTime umate;

  public Member(Long memberId, String email, String passwd) {
    this.memberId = memberId;
    this.email = email;
    this.passwd = passwd;
  }
}
