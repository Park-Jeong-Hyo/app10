package com.kh.app.web.form.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginForm {
  @NotBlank
  @Email
  @Size(min=5, max=50)
  private String email;
  @NotBlank
  @Size(min=4, max=12)
  //대문자, 숫자, 4~12자리
  @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,12}$")
  private String passwd;
}
