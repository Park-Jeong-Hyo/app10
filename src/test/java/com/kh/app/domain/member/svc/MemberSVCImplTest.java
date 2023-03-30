package com.kh.app.domain.member.svc;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MemberSVCImplTest {
  @Autowired
  MemberSVC memberSVC;

  @DisplayName("회원아이디 존재유무 확인")
  @Test
  void isExist() {
    boolean exist = memberSVC.isExist("test1@kh.com");
    Assertions.assertThat(exist).isTrue();

    exist = memberSVC.isExist("test11@kh.com");
    Assertions.assertThat(exist).isFalse();
  }
}