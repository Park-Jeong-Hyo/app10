package com.kh.app.domain.member.dao;

import com.kh.app.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j //log를 찍기 위함
@Repository //어플리케이션을 구동할 때 DAO라고 알려줌
@RequiredArgsConstructor //파이널인 멤버필드의 생성자를 만든다.
public class MemberDAOImpl implements MemberDAO{
  //파라미터를 ? 가 아닌 이름으로 연결하는 NamedParameterJdbcTemplate
  private final NamedParameterJdbcTemplate template;

//  public MemberDAOImpl(NamedParameterJdbcTemplate template) {
//    this.template = template;
//  }

  /**
   * @param member
   * @return
   */
  // controller에서 입력한 값이 member를 통해서 전달된다.
  // member는 dto(data transfer object)
  @Override
  public Member save(Member member) {
    //가변객체
    StringBuffer sql = new StringBuffer();
    //마지막 따옴표 앞 띄워주기 + 쿼리 끝 세미콜론 삭제
    sql.append("INSERT INTO member ( member_id, email, passwd, nickname, gender, hobby, region ) ");
    sql.append("VALUES ( MEMBER_MEMBER_ID_SEQ.nextval, :email, :passwd, :nickname, :gender, :hobby, :region ) ");
    //인터페이스 SqlParameterSource의 구현 BeanPropertySqlParameterSource
    //Bean...: 스프링에 의하여 생성되고 관리되는 자바 객체 bean(getter,setter/생성자), 자바 빈 규약을 따르는 클래스 제어의 역전(IOC),
    // dto 객체(Member)를 넘겨주면 자동으로 자바 객체 bean에 바인딩한다.
    //SqlParameter...: sql에 들어갈 parameter map객체를 처리하는 인터페이스
    SqlParameterSource param = new BeanPropertySqlParameterSource(member);
    //pk가 시퀀스를 통해서 만들어 지면 값이 존재하지 않기 때문에
    // keyholder라는 걸 통해서 값을  얻어낸다.
    KeyHolder keyHolder = new GeneratedKeyHolder(); // insert된 레코드에서 컬럼값 추출
    //4번째 값이 키 이름, 인서트된 값을 3번째 키홀더가 가지고 있음.
    //1. sql문자열, 2. map like 객체(키,값) 3. 키 값, 4. 칼럼 이름
    template.update(sql.toString(), param, keyHolder, new String[]{"member_id"});

    // 아이디 추출, keyholder로 부터 가져온 키 값으로부터
    long memberId = keyHolder.getKey().longValue();
    // 키 값이 없던 member에 키값을 설정
    member.setMemberId(memberId);
    return member;
  }
  /**
   * @param memberId
   * @param member
   */
  @Override
  public void update(Long memberId, Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("   set nickname = ?, ");
    sql.append("       gender = ?, ");
    sql.append("       hobby = ?, ");
    sql.append("       region = ? ");
    sql.append(" where member_id = ? ");
    sql.append(" where email = ? ");

    //mapSqlParameterSource는 addValue를 사용할 수 있다.
    //update에서 반영하는 dto(member)는 id를 들고 올 수가 없다.
    //id를 반영할 수가 없기 때문에 param에 id를 넣기 위해서 MapSql...를 사용
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("nickname",member.getNickname())
        .addValue("gender",member.getGender())
        .addValue("hobby",member.getHobby())
        .addValue("region",member.getRegion())
        .addValue("member_id",memberId)
        .addValue("email",member.getEmail());

    template.update(sql.toString(),param);
  }

  /**
   * @param email
   * @return
   */
  @Override
  public Optional<Member> findByEmail(String email) {
    StringBuffer sql = new StringBuffer();
    sql.append("select member_id, ");
    sql.append("       email, ");
    sql.append("       passwd, ");
    sql.append("       nickname, ");
    sql.append("       gender, ");
    sql.append("       hobby, ");
    sql.append("       region ");
    sql.append("  from member ");
    sql.append(" where email = :email ");

    try {
      Map<String, String> param = Map.of("email", email);
      Member member = template.queryForObject(
          sql.toString(),
          param,
          BeanPropertyRowMapper.newInstance(Member.class)
      );
      return Optional.of(member);
    } catch (EmptyResultDataAccessException e) {
      //조회결과가 없는 경우
      return Optional.empty();
    }
  }

  /**
   * @param memberId
   * @return
   */
  @Override
  public Optional<Member> findById(Long memberId) {
    StringBuffer sql = new StringBuffer();
    sql.append("select member_id as memberId, ");
    sql.append("       email, ");
    sql.append("       passwd, ");
    sql.append("       nickname, ");
    sql.append("       gender, ");
    sql.append("       hobby, ");
    sql.append("       region ");
    sql.append("  from member ");
    sql.append(" where member_id = :member_id ");

    try {
      Map<String, Long> param = Map.of("member_id", memberId);
      Member member = template.queryForObject(
          sql.toString(),
          param,
          BeanPropertyRowMapper.newInstance(Member.class)
      );
      return Optional.of(member);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  /**
   * @return
   */
  @Override
  public List<Member> findAll() {
    StringBuffer sql = new StringBuffer();

    sql.append("select member_id as memberId, ");
    sql.append("       email, ");
    sql.append("       passwd, ");
    sql.append("       nickname, ");
    sql.append("       gender, ");
    sql.append("       hobby, ");
    sql.append("       region ");
    sql.append("  from member ");
    sql.append(" order by member_id desc ");

    List<Member> list = template.query(
        sql.toString(),
        BeanPropertyRowMapper.newInstance(Member.class)
    );

    return list;
  }

  /**
   * @param email
   */
  @Override
  public void delete(String email) {
    StringBuffer sql = new StringBuffer();
    sql.append("delete from member ");
    sql.append(" where email = :email ");

    Map<String, String> param = Map.of("email", email);
    template.update(sql.toString(), param);
  }

  /**
   * @param email
   * @return
   */
  @Override
  public boolean isExist(String email) {
    boolean flag = false;
    String sql = "select count(email) from member where email = :email ";

    Map<String, String> param = Map.of("email", email);
    try {
      template.queryForObject(sql, param, Integer.class);
      flag = true;
    } catch (EmptyResultDataAccessException e) {
      flag = false;
    }
    return flag;
  }

  /**
   * @param email
   * @param passwd
   * @return
   */
  @Override
  public Optional<Member> login(String email, String passwd) {
    StringBuffer sql = new StringBuffer();
    sql.append("select member_id, email, nickname, gubun ");
    sql.append("  from member ");
    sql.append(" where email = :email and passwd = :passwd ");

    Map<String, String> param = Map.of("email", passwd,"passwd",passwd);
    // 레코드1개를 반환할경우 query로 list를 반환받고 list.size() == 1 ? list.get(0) : null 처리하자!!
    List<Member> list = template.query(
        sql.toString(),
        param,
        BeanPropertyRowMapper.newInstance(Member.class) //자바객체 <=> 테이블 레코드 자동 매핑
    );

    return list.size() == 1 ? Optional.of(list.get(0)) : Optional.empty();
  }

  /**
   * @param nickname
   * @return
   */
  @Override
  public Optional<String> findEmailByNickname(String nickname) {
    StringBuffer sql = new StringBuffer();
    sql.append("select email ");
    sql.append("  from member ");
    sql.append(" where nickname = :nackname ");

    Map<String, String> param = Map.of("nackname", nickname);
    List<String> result = template.query(
        sql.toString(),
        param,
        (rs, rowNum)->rs.getNString("email")
    );

    return (result.size() == 1) ? Optional.of(result.get(0)) : Optional.empty();
  }

}