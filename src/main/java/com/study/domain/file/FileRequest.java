package com.study.domain.file;

import lombok.Builder;
import lombok.Getter;

// 파일 정보를 저장(INSERT)하는 용도로 사용할 파일 요청 클래스이다
// 물리적인 파일은 디스크에 저장되기 때문에, 파일의 부가적인 정보만 DB에 저장해 주면 된다.
@Getter
public class FileRequest {

    private Long id;                        // 파일 번호 (PK)
    private Long postId;                    // 게시글 번호 (FK)
    private String originalName;            // 원본 파일명
    private String saveName;                // 저장 파일명
    private long size;                      // 파일 크기

    // 생성자 메서드에 @Builder 어노테이션이 선언되어 있다. @Builder는 롬복에서 제공해주는 기능으로
    // 빌더 패턴(Build pattern)으로 객체를 생성할 수 있게 해준다. 빌더 패턴은 생성자 파라미터가 많은 경우게 가독성을 높여주기도 하고
    // 아래 코드와 같이 변수에 값을 넣어주는 순서를 달리하거나, 원하는 변수에만 값을 넣어 객체를 생성가능
    @Builder
    public FileRequest(String originalName, String saveName, long size) {
        this.originalName = originalName;
        this.saveName     = saveName;
        this.size         = size;
    }

    // setPostId() - 파일은 게시글이 생성(INSERT) 된 후에 처리되어야 한다.
    // 해당 메서드는 생성된 게시글 ID를 파일 요청 객체의 postId에 저장하는 용도로 사용된다.
    // 객체 생성 시점에 같이 처리하지 않고 set 메서드를 이용해서 처리하는 이유는
    public void setPostId(Long postId) {
        this.postId = postId;
    }

}
  /*
    // 일반적인 생성자를 통한 객체 생성
    FileRequest fileRequest = new FileRequest("테스트.txt", "abcdeadcde.txt", 10768);

    // 빌더 패턴을 통한 객체 생성 1
    FileRequest fileRequest1 = FileRequest.builder()
                                          .originalName("테스트.txt")
                                          .saveName("abcdeabcde.txt")
                                          .size(10768)
                                          .build();
    // 빌더 패턴을 통한 객체 생성 2
    FileRequest fileRequest2 = FileRequest.builder()
                                          .size(10768)
                                          .saveName("abcdeabcde.txt")
                                          .originalName("테스트.txt")
                                          .build();

    // 빌더 패턴을 통한 객체 생성 3
    FileRequest fileRequest3 = FileRequest.builder()
                                          .saveName("abcdeabcde.txt")
                                          .build();
     */
