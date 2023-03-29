package com.kh.app.web.form.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

//joinform의 view와 매칭하기위한 form객체
@Data
public class JoinForm {
  @Email
  private String email;
  @NotBlank
  @Size(min=4, max=12)
  private String passwd;
  @NotBlank
  @Size(min=4, max=12)
  private String passchk;
  @NotBlank
  @Size(max=10)
  // 이 이하는 고정값이 있기 때문에 설정할 필요가 있나>
  private String nickname;
  private String gender;
  // 2개 이상의 취미를 처리하기 위해
  private List<String> hobby;
  private String region;
}
