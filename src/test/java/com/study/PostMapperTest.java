/*
package com.study;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.study.domain.post.PostMapper;
import com.study.domain.post.PostRequest;
import com.study.domain.post.PostResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostMapperTest {

    // @Autowired를 이용해서 스프링 컨테이너에 등록된 PostMapper 빈(bean)을 클래스에 주입 DI
    @Autowired
    PostMapper postMapper;

    // 게시글 생성하는 메서드, PostRequest 객체를 생성, set() 메서드를 이용하여 값을 세팅
    // PostMapper save() 호출, 메서드 호출되면 PostMapper.xml의 save 쿼리가 실행
    // #{변수명} 표현식을 통해 PostRequest 객체의 멤버 변수에 접근
    @Test
    void save() {
        PostRequest params = new PostRequest(); // PostRequest 객체 생성
        params.setTitle("1번 게시글 제목"); // set 메서드를 이용하여 값을 세팅
        params.setContent("1번 게시글 내용");
        params.setWriter("테스터");
        params.setNoticeYn(false);
        postMapper.save(params); // postMapper.java save() 호출 및 실행 -> 값을 대입 -> PostMapper.xml의해서 #{변수명} 표현식을 통해 PostRequest 값에 대입 

        List<PostResponse> posts = postMapper.findAll();
        System.out.println("전체 게시글 개수는 : " + posts.size() + "개입니다.");
    }

    // post 객체는 앞에서 생성된 게시글의 PK인 1을 인자로 전달해서 게시글 상세정보 조회
    // postJson는 스프링부트에 기본으로 내장되어 있는 Jackson 라이브러리를 이용,
    // 조회한 1번 게시글의 응답(response) 객체를 JSON 문자열로 변환
    @Test
    void findById() {
        PostResponse post = postMapper.findById(1L);

        try {
            String postJson = new ObjectMapper().registerModule(new JavaTimeModule())
                                                .writeValueAsString(post);
            System.out.println(postJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        // 1. 게시글 수정
        // 게시글 수정하는 메서드, PostRequest 객체를 생성, set() 메서드를 이용하여 값 변경
        // 변경한 내용을 postMapper.java를 이용하여 update() 메서드 호출, 변경 값을 호출한 값에 담는다
        // 담을 값을 PostMapper.xml에 의해 담은 PostMapper.java의 내용을 PostRequest 객체의 멤버 변수에 대입
        // save() 다른 점은 id값이 필요
        PostRequest params = new PostRequest();
        params.setId(1L);
        params.setTitle("1번 게시글 제목 수정합니다");
        params.setContent("1번 게시글 내용 수정합니다");
        params.setWriter("SAK");
        params.setNoticeYn(true);
        postMapper.update(params);

        // 2. 게시글 상세정보 조회
        PostResponse post = postMapper.findById(1L);
        try {
            String postJson = new ObjectMapper().registerModule(new JavaTimeModule())
                                                .writeValueAsString(post);
            System.out.println(postJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        System.out.println("삭제 이전의 전체 게시글 개수는 : " + postMapper.findAll()
                                                              .size() + "개입니다.");
        postMapper.deleteById(1L);
        System.out.println("삭제 이후의 전체 게시글 개수는 : " + postMapper.findAll()
                                                              .size() + "개입니다.");
    }
}
 */