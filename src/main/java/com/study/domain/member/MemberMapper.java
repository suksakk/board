package com.study.domain.member;

import org.apache.ibatis.annotations.Mapper;

// 게시글, 댓글 CRUD 메서드와 구조가 유사, 상세정보를 조회하는 findByXXX() 메서드에서 PK(id)가 아닌
// UK(loginId)를 기준으로 쿼리가 실행되는 차이가 있다
@Mapper
public interface MemberMapper {

    /**
     * 회원 정보 저장 (회원가입)
     *
     * @param params - 회원 정보
     */
    void save(MemberRequest params);

    /**
     * 회원 상세정보 조희
     *
     * @param loginId - UK
     * @return 회원 상세정보
     */
    MemberResponse findByLoginId(String loginId);

    /**
     * 회원 정보 수정
     *
     * @param params - 회원 정보
     */
    void update(MemberRequest params);

    /**
     * 회원 정보 삭제
     *
     * @param id - PK
     */
    void deleteById(Long id);

    /**
     * 회원 수 카운팅 (ID 중복 체크)
     *
     * @param loginId - UK
     * @return 회원 수
     */
    int countByLoginId(String loginId);
}
