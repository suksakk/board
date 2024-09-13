package com.study.domain.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.common.dto.SearchDto;

// 데이터베이스와 통신 역할을 할 Mapper 인터페이스 즉 DAO = mapper.java, repository = mapper.java
// @Mapper - 과거에는 DAO(Date Access Object)클래스에 @Repository 어노테이션을 선언해서 해당 클래스가 DB와 통신하는 클래스임을 명시
// MyBatis는 Mapper(Java 인터페이스)와 XML Mapper(실제 DB에 접근해서 호출할 SQL 쿼리를 작성(선언)하는 파일)를 통해 DB와 통신
// 쉽게 말해 XML Mapper에 SQL 쿼리를 선언해 두고 Mapper.java를 통해 SQL 쿼리를 호출
// Mapper.java는 XML Mapper에 선언된 SQL 중에서 메서드명과 동일한 id를 가진 SQL 쿼리를 찾아 실행
// EX) 메서드명이 "savePost()" 라고 가정했을 때 SQL id는 "savePost"가 되어야 한다
// 가장 중요한건 Mapper에는 @Mapper 어노테이션을 필수적으로 선언해 주어야 하며,
// Mapper와 XML Mapper는 XML Mapper의 namespace라는 속성을 통해 연결된다는 점이다
@Mapper
public interface PostMapper {

	/**
	 * 게시글 저장
	 * @param params - 게시글 정보
	 */
	// save() - 게시글을 생성하는 INSERT 쿼리를 호출한다. 파라미터를 전달받는 params는
	// 요청(PostRequest) 클래스의 객체, params에는 저장할 게시글 정보가 담긴다
	void save(PostRequest params);

	/**
	 * 게시글 상세정보 조회
	 * @param id - PK
	 * @return 게시글 상세정보
	 */
	// findById() - 특정 게시글을 조회하는 SLELECT 쿼리를 호출한다.
	// 파라미터로 id(PK)를 전달받아 SQL 쿼리의 WHERE 조건으로 사용,
	// 쿼리가 실행되면 해당 id값에 맞는 메서드의 리턴 타입인 응답(PostResponse) 클래스 객체의 각 멤버변수에 결괏값이 매핑(바인딩)된다.
	PostResponse findById(Long id);

	/**
	 * 게시글 수정
	 * @param params - 게시글 정보
	 */
	// update() - 게시글 정보를 수정하는 UPDATE 쿼리를 호출
	// save()와 마찬가지로 요청(PostRequest) 클래스에 객체의 파라미터로 전달받고, params에는 수정할 게시글 정보가 담긴다.
	// save()와 다른점은 UPDATE 쿼리의 WHERE 조건문으로 게시글 번호 id(PK) 값이 필요하다
	void update(PostRequest params);

	/**
	 * 게시글 삭제
	 * @param id - PK
	 */
	// deleteById() - finedById와 마찬가지로 특정 게시글을 삭제하는 UPDATE 쿼리를 호출
	// id(PK)를 파라미터로 전달받아 SQL 쿼리의 WHERE 조건으로 사용하게 된다
	// SQL 쿼리가 실행되면 삭제 여부(delete_yn) 칼럼의 상태 값을 0(false)에서 1(true)로 업데이트
	void deleteById(Long id);

	/**
	 * 게시글 리스트 조회
	 * @return 게시글 리스트
	 */
	// findAll() - 게시글 목록을 조회하는 SELECT 쿼리를 조회 findById()는 id(PK)를 기준으로 하나의 게시글을 조회
	// 해당 메서드는 여러 개의 게시글을(PostResponse) 리스트(List)에 담아서 리턴해주는 역할
	List<PostResponse> findAll(SearchDto params);

	/**
	 * 게시글 수 카운팅
	 * @return 게시글 수
	 */
	int count(SearchDto params);

	/**
	 * 게시글 조회 수
	 * @param id - PK
	 */
	void viewCnt(@RequestParam("id") final Long id);
}
