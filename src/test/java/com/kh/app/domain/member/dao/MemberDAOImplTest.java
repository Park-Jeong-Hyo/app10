package com.kh.app.domain.member.dao;

import com.kh.app.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest //스프링부트환경을 가지고 테스트를 하겠다는 어노테이션
class MemberDAOImplTest {

  // 해당 타입으로 생성된 인스턴스가 스프링 빈에 있으면 걔
  // 를 참조할 수 있게 된다.
  @Autowired
  private MemberDAO memberDAO;
  @Test
  void save() {
    Member member = new Member();
    member.setEmail("test3@kh.com");
    member.setPasswd("1234");
    member.setNickname("별칭2");
    member.setGender("남자");
    member.setHobby("농구");
    member.setRegion("울산");
    //컨트롤 p하면 매개변수가 어떤게 와야되는 지 알 수 있다.
    Member savedMember = memberDAO.save(member);
    //assertj에 있는 assertions
    Assertions.assertThat(savedMember.getMemberId()).isGreaterThan(0);
    Assertions.assertThat(savedMember.getEmail()).isEqualTo("test3@kh.com");
    Assertions.assertThat(savedMember.getPasswd()).isEqualTo("1234");
    Assertions.assertThat(savedMember.getNickname()).isEqualTo("별칭2");
    Assertions.assertThat(savedMember.getGender()).isEqualTo("남자");
    Assertions.assertThat(savedMember.getHobby()).isEqualTo("농구");
    Assertions.assertThat(savedMember.getRegion()).isEqualTo("울산");
  }
}