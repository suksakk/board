package com.study.domain.post;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.study.common.dto.SearchDto;
import com.study.common.paging.Pagination;
import com.study.common.paging.PagingResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

// 서비스는 MVC 패턴 중 M(Model)에 해당 사용자의 요구사항을 처리하는 로직을 실행하는 핵심 영역
// @Service - PostMapper 인터페이스의 @Mapper와 유사, 해당 클래스가 비즈니스 로직을 담당하는 Service Layer 클래스 의미
// @RequiredArgsConstructor - 과거에 스프링 레거시는 일반적으로 @Autowired, @Inject, @Resource 등 이용 Bean을 주입
// 스프링부트는 생성자로 Bean을 주입 방식 권장 해당 기능은 롬복에서 제공 클래스내에 final로 선언된 모든 멤버에 대한 생성자 자동으로 만들어준다
@Service
@RequiredArgsConstructor // 모든 필드에 생성자 자동 생성
public class PostService {

  private final PostMapper postMapper; // 이전 글에서 처리한 게시글 CRUD 기능을 포함하고 있는 Mapper 인터페이스

  @Transactional
  public void viewCnt(Long id) {
    postMapper.viewCnt(id);
  }

  /**
   * 게시글 저장
   * 
   * @param params - 게시글 정보
   * @return Generated PK
   */
  // 스프링에서 제공하는 트랜잭션 처리방법, "선언적 트랜잭션으로 불림 호출된 메서드에 해당 어노테이션이 선언되어 있으면 메서드의 실행과 동시에
  // 트랜잭션 시작,
  // 메서드의 정상 종료 여부에 따라 Commit 또는 Rollback 됩니다.
  @jakarta.transaction.Transactional
  public Long savePost(final PostRequest params) { // savePost() 게시글 생성, INSERT 완료시 생성된 게시글 id 리턴
    postMapper.save(params); // postMapper의 save() 함수 호출
    return params.getId(); // 생성된 게시글 ID가 null이 되지 않기 위해서는 PK값을 객체에 담아줘야한다 이것을 MyBatis의 "useGeneratedKeys"
                           // 기능 이용 --> PostMapper.xml 이동
  }

  /**
   * 게시글 상세정보 조회
   * 
   * @param id - PK
   * @return 게시글 상세정보
   */
  public PostResponse findPostById(final Long id) { // findById() 특정 게시글의 상세정보 조회
    postMapper.findById(id);
    return postMapper.findById(id);
  }

  /**
   * 게시글 수정
   * 
   * @param params - 게시글 정보
   * @return PK
   */
  @Transactional
  public Long updatePost(final PostRequest params) { // updatePost() 게시글의 수정 UPDATE 완료 시, 게시글 id 리턴
    postMapper.update(params);
    return params.getId();
  }

  /**
   * 게시글 삭제
   * 
   * @param id - PK
   * @return PK
   */
  public Long deletePost(final Long id) { // deletePost() 게시글 id에 따라 게시글 삭제 게시글 id 리턴
    postMapper.deleteById(id);
    return id;
  }

  /**
   * 게시글 리스트 조회
   * 
   * @param params - search conditions
   * @return list & pagination information
   */
  public PagingResponse<PostResponse> findAllPost(final SearchDto params) {

    // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
    int count = postMapper.count(params);
    if (count < 1) {
      return new PagingResponse<>(Collections.emptyList(), null);
    }

    // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
    Pagination pagination = new Pagination(count, params);
    params.setPagination(pagination);

    // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
    List<PostResponse> list = postMapper.findAll(params);
    return new PagingResponse<>(list, pagination);
  }

}
