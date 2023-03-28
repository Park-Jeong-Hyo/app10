package com.kh.app.domain.member.dao;

import com.kh.app.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j //log를 찍기 위함
@Repository //어플리케이션을 구동할 때 DAO라고 알려줌
@RequiredArgsConstructor //파이널인 멤버필드의 생성자를 만든다.
public class MemberDAOImpl implements MemberDAO{
  private final NamedParameterJdbcTemplate template;

//  public MemberDAOImpl(NamedParameterJdbcTemplate template) {
//    this.template = template;
//  }

  /**
   * @param member
   * @return
   */
  @Override
  public Member save(Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append("INSERT INTO member ( member_id, email, passwd, nickname, gender, hobby, region ) ");
    sql.append("VALUES ( MEMBER_MEMBER_ID_SEQ.nextval, :email, :passwd, :nickname, :gender, :hobby, :region ) ");
    SqlParameterSource param = new BeanPropertySqlParameterSource(member);
    //pk가 시퀀스를 통해서 만들어 지면 값이 존재하지 않기 때문에
    // keyholder라는 걸 통해서 값을  얻어낸다.
    KeyHolder keyHolder = new GeneratedKeyHolder(); // insert된 레코드에서 컬럼값 추출
    //4번째 값이 키 이름, 인서트된 값을 3번째 키홀더가 가지고 있음.
    template.update(sql.toString(), param, keyHolder, new String[]{"member_id"});

    long memberId = keyHolder.getKey().longValue();

    member.setMemberId(memberId);
    return member;
  }

  /**
   * @param memberId
   * @param member
   */
  @Override
  public void update(Long memberId, Member member) {

  }

  /**
   * @param email
   * @return
   */
  @Override
  public Member findByEmail(String email) {
    return null;
  }

  /**
   * @param memberId
   * @return
   */
  @Override
  public Member findById(String memberId) {
    return null;
  }

  /**
   * @return
   */
  @Override
  public List<Member> findAll() {
    return null;
  }

  /**
   * @param email
   */
  @Override
  public void delete(String email) {

  }

  /**
   * @param email
   * @return
   */
  @Override
  public boolean isExist(String email) {
    return false;
  }

  /**
   * @param email
   * @param passwd
   * @return
   */
  @Override
  public Member login(String email, String passwd) {
    return null;
  }

  /**
   * @param nickname
   * @return
   */
  @Override
  public String findEmailBy(String nickname) {
    return null;
  }

  /**
   * @param memberId
   * @param passwd
   * @return
   */
  @Override
  public String findPasswdById(String memberId, String passwd) {
    return null;
  }
}
