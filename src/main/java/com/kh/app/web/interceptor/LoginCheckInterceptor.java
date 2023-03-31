package com.kh.app.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
  // 요청메세지정보는 httpservletrequest, 응답메세지 정보는 httpservletresponse,handler는 컨트롤러
  // 요청메세지, 응답메세지, 컨트롤러 3가지에 접근이 가능
  // 컨트롤러가 호출되기 전
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String redirectUrl = null;
    String requestURI = request.getRequestURI();
    // 어떤 url을 요청할 때 마다 log에 찍힌다.
    log.info("requestURI={}",requestURI);
    //요청 url에서 쿼리스트링 부분만 읽는 메소드
    //쿼리스트링이 있으면 if 아니면 else
    if(request.getQueryString() != null) {
      //쿼리 파라미터에 한글같은게 포함이 될 수 있기 때문에 깨지지 않게 하기 위함.
     String queryString = URLEncoder.encode(request.getQueryString(), "UTF-8");
      StringBuffer sb = new StringBuffer();
      redirectUrl = sb.append(requestURI)
         .append("&")
         .append(queryString)
         .toString();
    } else {
      //쿼리스트링이 없으면
      redirectUrl = requestURI;
    }

    //세션 체크
    // getSession(false)인 경우 세션이 있으면 읽어오고, 없으면 생성하지 않는다.
    // getSession(true)인 경우 세션이 있으면 읽어오고 없으면 생성한다.
    HttpSession session = request.getSession(false);
    if(session == null) {
      log.info("미인증 요청");
      // 상기의 if로직을 거쳐서 redirectUrl로 전달됨.
      response.sendRedirect("/login?redirectUrl=" + redirectUrl);
    }

    // return이 true여야 다음단계로 진행이 가능하다.
    return true;
  }

  // 여기서는 3가지 말고도 view까지 조작이 가능하다.
  //
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
