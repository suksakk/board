package com.study;

import com.study.common.dto.SearchDto;
import com.study.common.paging.PagingResponse;
import com.study.domain.post.PostResponse;
import com.study.domain.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// REST
// REST는 Representational State Transfer 약어로, 하나의 URI는 하나의 고유한 리소스를 대표하도록 설계된다는 개념
// REST API는 사용자가 어떠한 요청을 했을 때 HTML을 리턴하지 않고, 자바스크립트를 이용해서 사용자가 필요로 하는 결과만을 리턴해주는 방식

@RestController
@RequiredArgsConstructor
public class RestApiTestController {

    private final PostService postService;

    @GetMapping("/posts")
    public PagingResponse<PostResponse> findAllPost() {
        return postService.findAllPost(new SearchDto());
    }

    /*
    // 컨트롤러 메서드에 @ResponseBody가 붙으면, 스프링의 메시지 컨버터에 의해 화면이 아닌 리턴 타입에 해당하는 데이터 자체를 리턴
    @GetMapping("/members")
    @ResponseBody // public @ResponseBody List<Map<String, Object>> findAllMember() 와 같이 리턴 타입 앞에도 선언 가능
    public List<Map<String, Object>> findAllMember() {

        List<Map<String, Object>> members = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            Map<String, Object> member = new HashMap<>();
            member.put("id", i);
            member.put("name", i + "번 개발자");
            member.put("age", 10 + i);
            members.add(member);
        }

        return members;
    }
     */
}
