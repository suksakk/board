package com.study;

import com.study.domain.post.PostRequest;
import com.study.domain.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired // @Autowired 이용해서 스프링 컨테이너에 등록된 PostService 빈을 클래스에 주입
    PostService postService;

    // save() 코드를 실행시 테스트 성공은 하지만 생성된 게시글 ID가 null로 출력 id는 auto_increment에 의해
    // 자동으로 1씩 증가하여 데이터 생성, 이때 생성된 PK를 객체에 담아주기 위해서는 MyBatis의 useGeneratedKeys 기능 이용
    @Test
    void save() {
        PostRequest params = new PostRequest();
        params.setTitle("1번 게시글 제목");
        params.setContent("1번 게시글 내용");
        params.setWriter("테스터");
        params.setNoticeYn(false);

        Long id = postService.savePost(params);
        System.out.println("생성된 게시글 ID : " + id);
    }

    @Test
    void saveByForeach() {
        for (int i = 1; i <= 1000; i++) {
            PostRequest params = new PostRequest();
            params.setTitle(i + "번 게시글 제목");
            params.setContent(i + "번 게시글 내용");
            params.setWriter("테스터" + i);
            params.setNoticeYn(false);
            postService.savePost(params);
        }
    }
}
