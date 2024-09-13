package com.study.domain.post;

import java.time.LocalDateTime;

import lombok.Getter;

// 사용자에게 보여줄 데이터를 처리할 응답 클래스
// 테이블의 모든 칼럼을 멤버 변수로 선언해 주어야 한다 요청 클래스와 동일한 경로에 PostResponse 클래스를 추가 
@Getter
public class PostResponse {

	private Long id;                        // PK
	private String title;                   // 제목
	private String content;                 // 내용
	private String writer;                  // 작성자
	private int viewCnt;                    // 조회 수
	private Boolean noticeYn;               // 공지글 여부
	private Boolean deleteYn;               // 삭제 여부
	private LocalDateTime createdDate;      // 생성일시
	private LocalDateTime modifiedDate;     // 최종 수정일시

}
