package com.kh.app.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
//핸들러 인터셉터는 구성 메소드가 디폴트로 되어 있기 때문에 전부 다 구현할 필요는 없다.
public class MeasuringInterceptrot implements HandlerInterceptor{
  // 시간 차이를 구하기 위해서는 요청 메세지를 보낼 때, 모든 작업이 완료 되었을 때
  // 서로 시간을 빼서 차이만큼의 값이 요청시간이다.
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //요청 객체에 세터로 정보를 추가할 수 있다.
    request.setAttribute("beginTime", System.currentTimeMillis());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    //System.currentTimeMillis가 long타입이라서 long
    long beginTime = (long)request.getAttribute("beginTime");
    long endTime = System.currentTimeMillis();
    log.info(request.getRequestURI()+"실행시간:"+(endTime - beginTime));
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
//이렇게 완성했으면 인터셉터를 등록을 해야한다.(AppConfig)
