package com.kh.app;

import com.kh.app.web.interceptor.LoginCheckInterceptor;
import com.kh.app.web.interceptor.MeasuringInterceptrot;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//인터셉터를 등록하기 위한 클래스
//WebMvcConfigurer을 구현해야함. (정적메소드와 디폴트메소드로 되어있다. 그러므로 전부 구현할 필요가 없다)
@Configuration
public class AppConfig implements WebMvcConfigurer {
  //인터셉터를 등록하는 메소드를 오버라이드
  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    //실행 속도를 측정하는 인터셉터 등록
    registry.addInterceptor(new MeasuringInterceptrot())
        .order(1)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/",
            "/css/*",
            "/js/*",
            "/img/*",
            "/error/*"
            );
    //로그인 체크를 하는 인터셉터 등록
    registry.addInterceptor(new LoginCheckInterceptor())
        //인터페이스 실행 순서를 정해주는 메소드
        .order(2)
        //패턴을 통해서 내가 원하는 url만 포함시키는 메소드
        // /** / 아래의 모든 url을 인터셉터에 포함하겠다. => 모든 url을 포함하겠다.
        .addPathPatterns("/**")
        // 모든 경로를 포함하겠다고 했으나 일부 경로는 배제하겠다.
        .excludePathPatterns(
            "/",          //초기화면
            "/login",     //로그인
            "/logout",    //로그아웃
            "/members/add", //회원가입
            "/members/joinSuccess", //가입성공페이지
            "/css/*",
            "/js/*",
            "/img/*",
            "/error/*",
            "/api/*"
        ); // 이상 인터셉터에서 제외되는 url패턴
  }
}
