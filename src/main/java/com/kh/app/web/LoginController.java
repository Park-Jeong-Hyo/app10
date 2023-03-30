package com.kh.app.web;

import com.kh.app.domain.entity.Member;
import com.kh.app.domain.member.svc.MemberSVC;
import com.kh.app.web.form.login.LoginForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
  private final MemberSVC memberSVC;

  //로그인
  @GetMapping("/login")
  public String loginForm(Model model) {

    model.addAttribute("loginForm", new LoginForm());
    return "login";
  }
  //로그인 처리
  @PostMapping("/login")
  public String login(
      @Valid @ModelAttribute LoginForm loginForm,
      //bindingResult는 반드시 @Model... 보다 뒤에 와야함
      BindingResult bindingResult) {

    if(bindingResult.hasErrors()) {
      log.info("bindingResult", bindingResult);
      return "login";
    }
    //1) 아이디 존재 유무
      if(!memberSVC.isExist(loginForm.getEmail())) {
        //필드에러? reject는? = world error
        bindingResult.rejectValue("email","login" , "아이디가 존재하지 않습니다.");
        return "login";
      }
    //2) 로그인
    Optional<Member> loginMember = memberSVC.login(loginForm.getEmail(), loginForm.getPasswd());
    return "redirect:/";
  }
  //로그아웃
  @GetMapping
}
