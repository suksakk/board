package com.study.domain.file;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 게시글, 댓글, 회원과 달리 파일 업로드 CRUD는 필요한 메서드를 그때그때 추가하는 방향으로 진행
@Mapper
public interface FileMapper {

    /**
     * 파일 정보 저장
     *
     * @param files - 파일 정보 리스트
     */
    // saveAll() - 업로드된 파일의 정보를 DB에 저장한다. 여러 개의 파일정보를 한 번에 저장하기 위해 FileRequest를 List 타입으로 선언
    void saveAll(List<FileRequest> files);

    /**
     * 파일 리스트 조회
     *
     * @param postId - PK 리스트
     * @return 파일 리스트
     */
    // findAllByPostId() - 게시글 번호(postId)를 기준으로 게시글에 등록된 모든 첨부파일을 조회
    List<FileResponse> findAllByPostId(Long postId);

    /**
     * 파일 리스트 조회
     *
     * @param ids - PK 리스트
     * @return 파일 리스트
     */
    // findAllByIds() - 리스트 타입의 파일 번호(ids)를 기준으로 여러 개의 첨부파일을 조회한다.
    // 이 메서드는 물리적 파일의 삭제 처리에 사용되며, 용도는 뒤에서 설명
    List<FileResponse> findAllByIds(List<Long> ids);

    /**
     * 파일 삭제
     *
     * @param ids - PK 리스트
     */
    // deleteAllByIds() - 리스트 타입의 파일 번호(ids)를 기준으로 DB에서 첨부파일을 삭제처리한다.
    void deleteAllByIds(List<Long> ids);

    /**
     * 파일 상세정보 조회
     *
     * @param id - PK
     * @return 파일 상세정보
     */
    FileResponse findById(Long id);
}
