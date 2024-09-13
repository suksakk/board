package com.study.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;

// 회원가입(INSERT)과 회원정보 수정(UPDATE)에 사용될 요청 클래스 댓글 처리와 마찬가지로 비동기(Ajax)통신,
// 즉 JSON 포맷으로 데이터를 주고받기에 @Setter 어노테이션은 필요하지 않다
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequest {

    private Long id;                // 회원 번호 (PK)
    private String loginId;         // 로그인 ID
    private String password;        // 비밀번호
    private String name;            // 이름
    private Gender gender;          // 성별
    private LocalDate birthday;     // 생년월일

    public void encodingPassword(PasswordEncoder passwordEncoder) {
        if (StringUtils.isEmpty(password)) {
            return;
        }
        password = passwordEncoder.encode(password);
    }

}
