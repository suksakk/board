package com.study.common.paging;

import com.study.common.dto.SearchDto;
import lombok.Getter;

@Getter
public class Pagination {

    private int totalRecordCount;     // 전체 데이터 수
    private int totalPageCount;       // 전체 페이지 수
    private int startPage;            // 첫 페이지 번호
    private int endPage;              // 끝 페이지 번호
    private int limitStart;           // LIMIT 시작 위치
    private boolean existPrevPage;    // 이전 페이지 존재 여부
    private boolean existNextPage;    // 다음 페이지 존재 여부

    public Pagination(int totalRecordCount, SearchDto params) {
        if (totalRecordCount > 0) {
            this.totalRecordCount = totalRecordCount;
            calculation(params);
            // 로직해석 - 마지막 로직을 통해 params에 계산된 페이지 정보(this)를 저장,
            // 기존에는 서비스에서 처리 되던 로직이 Pagination 객체가 생성되는 시점에 실행되도록 변경
            // Pagination으로 책임을 전가함으로서 페이징 적용에 필요한 비즈니스 로직이 한 줄이 줄어들었다(코드 리펙터링)
            params.setPagination(this);
        }
    }

    private void calculation(SearchDto params) {

        // 전체 페이지 수 계산
        totalPageCount = ((totalRecordCount - 1) / params.getRecordSize()) + 1;

        // 현재 페이지 번호가 전체 페이지 수보다 큰 경우, 현재 페이지 번호에 전체 페이지 수 저장
        if (params.getPage() > totalPageCount) {
            params.setPage(totalPageCount);
        }

        // 첫 페이지 번호 계산
        startPage = ((params.getPage() - 1) / params.getPageSize()) * params.getPageSize() + 1;

        // 끝 페이지 번호 계산
        endPage = startPage + params.getPageSize() - 1;

        // 끝 페이지가 전체 페이지 수보다 큰 경우, 끝 페이지 전체 페이지 수 저장
        if (endPage > totalPageCount) {
            endPage = totalPageCount;
        }

        // LIMIT 시작 위치 계산
        limitStart = (params.getPage() - 1) * params.getRecordSize();

        // 이전 페이지 존재 여부 확인
        existPrevPage = startPage != 1;

        // 다음 페이지 존재 여부 확인
        existNextPage = (endPage * params.getRecordSize()) < totalRecordCount;
    }

}
