package com.study.common.dto;

import com.study.common.paging.Pagination;

import lombok.Getter;
import lombok.Setter;

// >>> 페이징은 사용자에게 데이터를 제공할 때, 전체 데이터중에 일부를 보여주는 방식이다
// 만약, 게시판에 등록된 게시글이 100,000개라고 가정했을 때, 한 페이지에서 100,000개의 데이터를 전부 출력할 수가 없다.
// 이러한 문제점의 페이징으로 해결할 수 있으며, 검색 기능을 이용해서 원하는 데이터만 조회할 수 있다. <<<
// 페이징과 검색 처리에는 몇 가지 파라미터(데이터)가 필요
// >>> page - 현재 페이지 번호를 의미하며, 페이지 정보 계산에 사용됩니다. <<<
// >>> recordSize - 페이지당 출력할 데이터 개수를 의마하며, page와 함계 페이지 정보를 계산에 사용 <<<
// >>> pageSize - 화면 하단에 출력할 페이지의 크기를 의미, 5로 지정하면 1~5까지, 10으로 지정하면 1~10까지 페이지를 보인다 <<<
// >>> keyword - 검색 키워드를 의미, MyBatis의 동적 SQL 처리에 사용
// >>> searchType - 검색 유형을 의미, keyword와 함께 검색처리에 사용 <<<
@Getter
@Setter
public class SearchDto {
	private int page;                                 // 현재 페이지 번호
	private int recordSize;                           // 페이지당 출력할 데이터 개수
	private int pageSize;                             // 화면 하단에 출력할 페이지 사이즈
	private String keyword;                           // 검색 키워드
	private String searchType;                        // 검색 유형
	private Pagination pagination;                    // 페이지네이션 정보
	private int viewCount;                            // 조회 수 카운트

	// >>> SearchDto() 생성자 - 객체가 생성되는 시점에 현재 페이지 번호는 1로,
	// 페이지당 출력할 데이터 개수와 하단에 출력할 페이지 개수를 10으로 초기화 <<<
	public SearchDto() {
		this.page = 1;
		this.recordSize = 10;
		this.pageSize = 10;
	}
}
