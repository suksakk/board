package com.study.domain.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Getter - 롬복이 제공해주는 기능으로, 클래스의 모든 멤버 변수에 대한 get() 메서드를 만든다
// NoArgsConstructor - 클래스의 기본 생성자를 만들어준다. access 속성을 이용해서 객체 생성을 protected로 제한
// 게시글 요청 클래스인 PostRequest에는 @Getter @Setter를 선언해서 사용 이전에 말했듯이 요청 클래스의 각 멤버 변수는 HTML의 폼 태그에 선언된 필드(input, textarea)의
// name 값을 기준으로 파라미터를 전송하며, 전송된 파라미터는 요청 클래스의 set() 메서드에 의해 값이 매핑
// 하지만 일반적이 REST API 방식은 데이터를 등록/수정 할 때 폼 자체를 전송하지 않고, key-value 구조로 이루어진 JSON 문자열 포맷으로 전송하여 set() 메서드 의해 값이 매핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    private Long id;           // 댓글 번호 (PK)
    private Long postId;       // 게시글 번호 (FK)
    private String content;    // 내용
    private String writer;     // 작성자

}
