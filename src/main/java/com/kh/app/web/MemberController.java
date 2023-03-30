package com.kh.app.web;

import com.kh.app.domain.common.util.CommonCode;
import com.kh.app.domain.entity.Code;
import com.kh.app.domain.entity.Member;
import com.kh.app.domain.member.svc.MemberSVC;
import com.kh.app.web.common.CodeDecode;
import com.kh.app.web.form.member.JoinForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberSVC memberSVC;
  private final CommonCode commonCode;

  //메소드레벨의 ModelAttribute, 이 컨트롤러를 이용하는 모든 view에서
  //괄호안의 이름으로 접근이 가능하다.
  @ModelAttribute("hobbies")
  public List<CodeDecode> hobbies(){
    List<CodeDecode> codes = new ArrayList<>();
//    codes.add(new CodeDecode("독서","독서"));
//    codes.add(new CodeDecode("수영","수영"));
//    codes.add(new CodeDecode("등산","등산"));
//    codes.add(new CodeDecode("골프","골프"));
    List<Code> findedCodes = commonCode.findCodesByPcodeId("A01");
    //case1)
    //  findedCodes.stream()
    //    .forEach(ele->codes.add(new CodeDecode(ele.getCodeId(),ele.getDecode())));
    //case2)
    for(Code code : findedCodes){
      codes.add(new CodeDecode(code.getCodeId(),code.getDecode()));
    }
    return codes;
  }

  @ModelAttribute("regions")
  public List<CodeDecode> regions(){
    List<CodeDecode> codes = new ArrayList<>();
//    codes.add(new CodeDecode("서울","서울"));
//    codes.add(new CodeDecode("부산","부산"));
//    codes.add(new CodeDecode("대구","대구"));
//    codes.add(new CodeDecode("울산","울산"));

    List<Code> findedCodes = commonCode.findCodesByPcodeId("A02");
    findedCodes.stream()
        .forEach(ele->codes.add(new CodeDecode(ele.getCodeId(),ele.getDecode())));
    return codes;
  }

  //회원가입양식
  @GetMapping("/add")
  //빈객체를 하나 생성, 렌더링을 할 때 아예 전달되는게 없으면 오류가 남
  public String joinForm(Model model) {
    model.addAttribute("joinForm", new JoinForm());
    return "member/joinForm";
  }

  //회원가입처리
  @PostMapping("/add")
  public String join(
      //모델 객체를 생성, 타입의 대문자는 키, 소문자로, 객체는 값으로
      @ModelAttribute  JoinForm joinForm,
      BindingResult bindingResult
//      Model model
      ) {
    log.info("joinForm={}", joinForm);
//    model.addAttribute("key", joinForm);

    if (bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "member/joinForm";
    }

    //비밀번호 체크(글로벌 에러이다. 필드에러는 rejectValue)
    if(!joinForm.getPasswd().equals(joinForm.getPasschk())) {
      bindingResult.reject("passwd","비밀번호가 동일하지 않습니다.");
      return "member/joinForm";
    }

      Member member = new Member();
    member.setEmail(joinForm.getEmail());
    member.setNickname(joinForm.getNickname());
    member.setPasswd(joinForm.getPasswd());
    member.setGender(joinForm.getGender());
    member.setHobby(hobbyToString(joinForm.getHobby()));
    member.setRegion(joinForm.getRegion());

    memberSVC.save(member);
    return "member/joinSuccess";
  }


  private String hobbyToString(List<String> hobby) {
    return StringUtils.join(hobby, ",");
  }
}

