package com.study.domain.comment;

import com.study.common.dto.SearchDto;
import lombok.Getter;
import lombok.Setter;

// SearchDto를 상속받는 이유는 게시글 페이징 처리에서 생성했던 SearchDto 클래스 구조를
// 게시글과 마찬가지로 댓글도 SearchDto의 멤버 변수들을 이용해서 페이징을 적용한다.
// 댓글은 tb_comment 테이블의 post_id 기준으로 SELECT 하기 때문에 게시글 번호를 필수 파라미터로 가져간다
@Getter
@Setter
public class CommentSearchDto extends SearchDto {

    private Long postId;    // 게시글 번호 (FK)

}