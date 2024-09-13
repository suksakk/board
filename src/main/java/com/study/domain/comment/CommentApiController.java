package com.study.domain.comment;

import com.study.common.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// REST Controller는 화면(HTML)이 아닌 데이터 자체를 리턴한다,
// 댓글 데이터의 CRUD는 전부 게시글 상세 페이지에서 이루어지기 때문에 화면을 따로 구성할 필요 없이 데이터만 주고 받는다
// @RestController - 컨트롤러의 모든 메서드에 @ResponseBody가 적용, 응답으로 페이지(HTML)가 아닌 리턴 타입에 해당하는 데이터(객체) 자체를 리턴
// @PathVariable - REST API에서 리소스를 표현하는데 사용, 해당 어노테이션을 이용 시 URI에서 템플릿 형태로 파라미터(변수)를 전달받을 수 있다.
// saveComment()는 URI에서 게시글 번호(postId)를 수집하며, postId는 @PathVariable로 선언한 Long 타입의 postId에 매핑
// @RequestBody - 일반적으로 데이터를 생성(POST) 또는 수정(PUT or PATCH)하는 경우 사용 저장할 데이터를 JSON 포맷으로 해서 서버에 요청하면, key-value 구조로 이루어진
// 각각의 데이터가 java 객체와 매핑
// save()Comment - 새로운 댓글을 생성한 후 생성된 댓글의 상세정보를 리턴
@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    // 신규 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public CommentResponse saveComment(@PathVariable(value = "postId", required = false) final Long postId,
                                       @RequestBody final CommentRequest params) {
        Long id = commentService.saveComment(params);
        return commentService.findCommentById(id);
    }

    // 댓글 리스트 조회
    // 로직 해석 - 해당 메서드는 REST API 설계 규칙에서 컬렉션(Collection)에 해당하는 기능으로, 특정 게시글(postId)에 등록된 모든 댓글을 조회합니다
    // 댓글 등록(유형) - POST(메서드) - /comments(올바른 표현) - /comments/insert(잘못된 표현)
    // 댓글 상세정보 조회(유형) - GET(메서드) - /comments/1(올바른 표현) - /comments/select/1(잘못된 표현)
    // 댓글 수정(유형) - PUT or PATCH(메서드) - /comments/1(올바른 표현) - /comments/update/1(잘못된 표현)
    // 댓글 삭제(유형) - DELETE(메서드) - /comments/1(올바른 표현) - /comments/delete/1(잘못된 표현)
    // 댓글 리스트 조회(유형) - GET(메서드) - /comments/1(올바른 표현) - /comments/(잘못된 표현)
    @GetMapping("/posts/{postId}/comments")
    public PagingResponse<CommentResponse> findAllComment(@PathVariable(value = "postId", required = false) final Long postId,
                                                          final CommentSearchDto params) {
        return commentService.findAllComment(params);
    }

    // 댓글 상세정보 조회
    @GetMapping("/posts/{postId}/comments/{id}")
    public CommentResponse findCommentById(@PathVariable(value = "postId", required = false) final Long postId,
                                           @PathVariable(value = "id", required = false) final Long id) {
        return commentService.findCommentById(id);
    }

    // 기존 댓글 수정
    // findCommentById - REST API 설계 규칙에서 도큐먼트(Document)에 해당하는 기능으로 특정 게시글(postId)에 등록된 모든 댓글중 PK(id)에 해당하는 댓글 조회
    // 댓글을 수정할 때 사용자에게 기존 댓글 정보를 보여주는 용도로 사용
    // updateComment - REST API 설계 규칙에서 도큐먼트(Document)에 해당하는 기능으로 특정 게시글
    // 댓글 수정이 완료되면 수정된 댓글 정보(객체)를 리턴, saveComment()와 마찬가지로 @RequestBody를 이용해서 JSON 문자열로 넘어오는 댓글 정보를
    // CommentRequest 객체의 각 멤버 변수에 매핑(바인딩)한다
    @PatchMapping("/posts/{postId}/comments/{id}")
    public CommentResponse updateComment(@PathVariable(value = "postId", required = false) final Long postId,
                                         @PathVariable(value = "id", required = false) final Long id,
                                         @RequestBody final CommentRequest params) {

        commentService.updateComment(params);
        return commentService.findCommentById(id);

    }

    // 댓글 삭제
    // 로직 해설 - REST API 설계 규칙에서 도큐먼트(document)에 해당하는 기능,
    // 특정 게시글(postId)에 등록된 모든 댓글 중 PK(id)에 해당하는 댓글을 삭제합니다. 삭제 프로세스가 완료되면 삭제된 댓글의 PK(id)를 리턴
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public Long deleteComment(@PathVariable(value = "postId", required = false) final Long postId,
                              @PathVariable(value = "id", required = false) final Long id) {
        return commentService.deleteComment(id);
    }
}


