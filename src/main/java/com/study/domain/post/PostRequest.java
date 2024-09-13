package com.study.domain.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

// @Getter/@Setter - 클래스에 선언된 두 어노테이션은 롬복 라이브러리에서 제공해 주는 기능으로,
// 클래스에 선언된 모든 멤버 변수에 대한 getter와 setter를 생성해 주는 역할
@Getter
@Setter
public class PostRequest {

	// 클래스의 멤버 변수가 칼럼 개수보다 적은 이유
	// 테이블 구조에서 사용자가 게시판에 글을 작성할 때 입력(선택)하는 필드는 제목, 내용, 작성자, 공지글 여부이다
	// id는 auto_increment 속성에 의해 자동으로 1씩 증가되므로 게시글 생성 시점에는 필요 없음
	// 그러나 게시글 수정(UPDATE) 할 때 SQL 쿼리의 WHERE 조건으로 id(PK)를 사용하기 때문에 선언 
	private Long id;                                            // PK
	private String title;                                       // 제목
	private String content;                                     // 내용
	private String writer;                                      // 작성자
	private Boolean noticeYn;                                   // 공지글 여부
	private int viewCnt;

	// 게시글을 저장하면 PostController의 savePost()가 실해되고, FileUtils로 사용자가 업로드 할 파일(MultipartFile)을 전달하려면,
	// 가장 먼저 PostController의 savePost()에서 파라미터를 수집해야 한다
	// 컨트롤러의 메서드에 파라미터를 추가해도 되지만, 요청 클래스에서 한꺼번에 처리하는 게 가독성이 좋다
	private List<MultipartFile> files = new ArrayList<>();      // 첨부파일 List
	private List<Long> removeFileIds = new ArrayList<>();       // 삭제할 첨부파일 id List

}
