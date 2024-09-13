package com.study.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// Interceptor - 무언가를 가로챈다
// 인터셉터는 컨트롤러의 메서드(URI)에 접근하는 과정에서 무언가를 제어할 필요가 있을 때 사용
// 정확히는 컨트롤러에 접근하기 전과 후로 나뉜다 ex) 회원제 시스템과 같은 경우, 로그인 또는 계정의 권한과 관련된 로직을 인터셉터를 이용해서 효율적으로 처리가능
// 목적 - HTTP 요철과 응답 사이클에서 요청과 응답을 처리하고 제어하는 데 중점을 둔다.
// 적용범위 - 웹 어플리케이션의 요청-응답 처리 과정에 맞춰서 동작
// 기능 - 요청 URI, 요청 파라미터, 응답 상태 기록, 인증 권한 체크와 같은 웹 요청 관련 처리
// EX) 요청 URI, HTTP 메서드, 요청 파라미터를 로그에 기록, 인증된 사용자인지 확인하고, 그렇지 않으면 요청 차단

// Slf4j - 롬복에서 제공해주는 어노테이션, 로깅 추상화 라이브러리이다
// 로깅 추상화란 로깅을 직접 구현체를 찾아 기능을 사용할 수 있게 해주는 것을 의미
@Slf4j
public class LoggerInterceptor implements HandlerInterceptor { // 인터셉터는 HandlerInterceptor 인터페이스를 implements 해서 구현

	// HandlerInterceptor 인터페이스는 preHandle(), postHandle(), afterHandle() 총 세 개의 추상메서드 포함

	// preHandle() - 컨트롤러의 메서드에 매핑된 특정 URI가 호출됐을 때 실행되는 메서드로, 컨트롤러를 경유(접근)
	// 하기 직전에 실행되는 메서드입니다. 우리는 사용자가 어떠한 기능을 수행했는지 파악하기 위해, 해당 메서드(기능)와 매핑된 URI 정보가 로그로 출력처리
	// HttpServletRequest - 컨트롤러에 인식된 링크값을 가져옴, HttpServletResponse - 인식된 링크값을 응답
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		log.debug("===============================================");
		log.debug("==================== BEGIN ====================");
		log.debug("Request URI ===> " + request.getRequestURI());
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	// postHandle() - 컨트롤러를 경유(접근) 한 후, 즉 화면(view)으로결과를 전달하기 전에 실행되는 메서드
	// preHandle()과 반대로 요청(Request)의 끝을 알리는 로그가 콘솔에 출력되도록 처리
	// 즉 게시판을 만들고 있으면 게시판 내용을 보여준다.
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		log.debug("==================== END ======================");
		log.debug("===============================================");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
