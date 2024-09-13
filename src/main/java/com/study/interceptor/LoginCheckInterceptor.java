package com.study.interceptor;

import com.study.domain.member.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 1. 세션에서 회원 정보 조회
        // MemberController의 login()에서 세션에 저장된 회원 정보 조회
        HttpSession session = request.getSession();
        MemberResponse member = (MemberResponse) session.getAttribute("loginMember");

        // 2. 회원 정보 체크
        // member 객체가 null이면 로그인 되어있지 않음을 의미 2번의 조건이 하나라도 true인 경우 사용자를 로그인 페이지로 이동
        // 이를 통해 url을 입력해서 강제로 게시판에 접근하는 사용자를 막을 수 있다.
        if (member == null || member.getDeleteYn() == true) {
            response.sendRedirect("/login.do");
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}