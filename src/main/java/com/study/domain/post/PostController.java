// 수정wlqrlqrjwqwr wtf??
package com.study.domain.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.common.dto.MessageDto;
import com.study.common.dto.SearchDto;
import com.study.common.file.FileUtils;
import com.study.common.paging.PagingResponse;
import com.study.domain.file.FileRequest;
import com.study.domain.file.FileResponse;
import com.study.domain.file.FileService;

import lombok.RequiredArgsConstructor;

// >>> MVC 패턴 중 C(Controller)에 해당, Model(서비스)과 View(UI == 화면(HTML))의 중간 다리 역할 영역
// 화면에서 사용자의 요청이 들어오면 가장 먼저 컨트롤러를 경유한다 컨트롤러는 사용자의 요구사항을 처리해 줄 서비스의 메서드(비즈니스 로직)를 호출,
// 그에 대한 실행 결과를 다시 화면으로 전달 <<<
@Controller // >>> @Controller 해당 클래스가 사용자의 요청과 응답을 처리하는 컨트롤러 클래스 의미 <<<
@RequiredArgsConstructor
public class PostController {

	@Autowired
	private final PostService postService;
	// PostRequest에서 설명했다시피, 파일 업로드는 게시글 생성이 완료 된 후에 처리 되어야 한다
	// 우선은 PostController의 멤버로 FileService와 FileUtils를 추가
	private final FileService fileService;
	private final FileUtils fileUtils;

	// 사용자에게 메시지 전달, 페이지를 리다이렉트 한다.
	private String showMessageAndRedirect(final MessageDto params, Model model) {
		model.addAttribute("params", params);
		return "common/messageRedirect";
	}

	// 로직 해석 - 해당 메서드는 deletePost()에서 수집한 이전 페이지 정보(queryParams의 모든 멤버)를 Map에 담아 리턴해주는 역할
	// MessageDto의 생성자는 마지막 파라미터로 Map을 전달받아 객체를 생성하기 때문에 컨트롤러에서 해당 메서드 추가
	private Map<String, Object> queryParamsToMap(final SearchDto queryParams) {
		Map<String, Object> data = new HashMap<>();
		data.put("page", queryParams.getPage());
		data.put("recordSize", queryParams.getRecordSize());
		data.put("pageSize", queryParams.getPageSize());
		data.put("keyword", queryParams.getKeyword());
		data.put("searchType", queryParams.getSearchType());
		return data;

	}

	// "게시글 작성 페이지"
	// >>> @GetMapping - "신규 게시글에는 게시글 번호(id)가 필요없다 따라서 "required 속성을 false"라 한다
	// 스프링은 과거 컨트롤러 메서드에 URI와 HTTP 요청 메서드를 매핑하기위해 @RequestMapping 사용 value에는 URI method에는 http 요청 메서드 지정 <<<
	// >>> Model - 메서드의 파라미터로 선언된 Model 인터페이스는 데이터를 화면(HTML)으로 전달하는데 사용 <<<
	// >>> @RequestParam - 화면(HTML)에서 보낸 파라미터를 전달받는 데 사용
	// 	ex) 신규게시글을 등록하는 경우에는 게시글 번호가 null로 전송된다. 하지만, 기존게시글을 수정하는 경우. 이거 수정이요
	// 수정할 게시글 번호(id)가 openPostWrite()의 파라미터로 전송, 전달받은 게시글 번호(id)를 이용 게시글 상세정보 조회후 화면 전달 <<<
	@GetMapping("/post/write.do")
	public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model) {
		if (id != null) { // id에 값이 있다면 조회한 게시글 상세 정보를 이용해서 post라는 이름으로 화면 전달
			PostResponse post = postService.findPostById(id);
			model.addAttribute("post", post);
		}
		return "post/write";
		// >>> 리턴 타입 - 컨트롤러 메서드는 void, String, ModelAndView, Map, List등 어떤 타입이던 리턴 타입으로 선언할 수 있다
		// 일반적으로 사용자가 보는 화면(HTML)을 처리할 때는 리턴 타입을 String으로 선언하고, 리턴 문에 HTML 파일의 경로를 선언 <<<

		// 전체 로직
		// >>> 게시글 번호(id)를 파라미터로 전달받은 경우, 즉 기존 게시글을 수정하는 경우에는 게시글 번호(id)를 이용해서
		// 조회한 게시글 상세 정보(응답 객체)를 post라는 이름으로 해서 화면으로 전달 <<<
		/*--------------------------------------------------------------------------------------------------------------*/
		// Model 인터페이스의 addAttribute() 메서드 이용하여 화면(HTML)으로 데이터 전달 가능
		// 해당 메서드는 이름(String name), 값(Object value) 두 개 *파라미터를 필요
		// 일반적으로 이름과 값은 동일하게 지정해 주는 게 코드를 읽기 유리 HTML에서는 ${} 표현식을 이용해 전달받은 데이터에 접근 가능
		// String title = "제목", content = "내용", writer = "SAK";
		//   model.addAttribute("t", title);
		//   model.addAttribute("c", content);
		//   model.addAttribute("w", writer);
		//   컨트롤러 메서드는 void, String, ModelAndView, Map, List등 어떤 타입이던 리턴 타입으로 선언 가능
		//   일반적으로 사용자가 보는 화면 (HTML) 처리할때는 리턴 타입을 String 선언, 리턴 문에 HTML 파일 경로 선언
	}

	// 게시글 리스트 페이지
	// >>> @GetMapping - GET 방식의 HTTP 요청 메서드를 의미합니다. 데이터 조회하거나 화면을 리턴하는 경우 GET 방식 이용 <<<
	// >>> posts - findAllPost()의 실행 결과를 담은 게시글 리스트 객체, Model 인터페이스의 addAttribute()를 이용해 "posts" 라는 이름으로 리스트 데이터를 화면(HTML)으로 전달 <<<
	/* ↑ 수정 전 -------------------------------------------------------------------------------------------------------------------------------------- */
	// >>> @ModelAttribute - 1:1로 매핑되는 단일 파라미터는 @RequestParam으로 전달받은 후 Model의 addAttribute()를
	// 이용해서 뷰(HTML)로 전달, 그러나 @ModelAttribute를 이용하면 파라미터로 수집한 객체를 자동으로 뷰까지 전달 가능
	// @ModelAttribute("params")의 "params"는 뷰에서 사용할 별칭, 파라미터를 "a", SearchDto params로 선언시
	// 뷰(HTML)에서는 "${a.page}같이 객체에 접근가능 <<<
	// 게시글 리스트 페이지
	@GetMapping("/post/list.do")
	public String openPostList(@ModelAttribute("params") final SearchDto params, Model model) {
		PagingResponse<PostResponse> response = postService.findAllPost(params);
		model.addAttribute("response", response);
		model.addAllAttributes(queryParamsToMap(params));
		return "post/list";
	}

	// 게시글 상세 페이지
	// >>> id - PostMapper -> findById 쿼리의 WHERE 조건으로 사용되는 게시글 번호(PK)입니다. <<<
	// >>> post - PostService -> findById()의 실행결과(특정 게시글의 상세정보)를 담은 게시글 응답 객체
	// 화면(HTML)에서는 "${post.변수명}"으로 데이터 접근 <<<
	@GetMapping("/post/view.do")
	public String openPostView(@RequestParam(value = "id") final Long id, Model model) {
		postService.viewCnt(id); // 조회 수 증가
		PostResponse post = postService.findPostById(id);
		model.addAttribute("post", post); // "post"가 view.html의 ${post}란 의미로 사용
		return "post/view"; // write.html, list.html과 마찬가지로 상세 화면(HTML)의 경로를 의미
	}

	// 신규 게시글 생성
	// >>> params - 폼 태그는 사용자 입력(선택) 필드의 "name" 값을 통해 컨트롤러 메서드로 파라미터 전송
	//	요청 객체(PostRequest)의 멤버 변수명과 사용자 입력 필드의 "name" 값이 동일하면
	//	PostRequest 타입의 객체인 params의 각 멤버변수에 "name" 값을 통해 전달된 필드의 value가 매핑 <<<
	@PostMapping("/post/save.do")
	public String savePost(final PostRequest params, Model model) {
		Long id = postService.savePost(params);
		List<FileRequest> files = fileUtils.uploadFiles(params.getFiles());
		fileService.saveFiles(id, files);
		MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/post/list.do", RequestMethod.GET, null);
		return showMessageAndRedirect(message, model);
	}

	// 기존 게시글 수정
	@PostMapping("/post/update.do")
	public String updatePost(final PostRequest params, final SearchDto queryParams, Model model) {

		// 1. 게시글 정보 수정
		postService.updatePost(params);

		// 2. 파일 업로드 (to Disk)
		List<FileRequest> uploadFiles = fileUtils.uploadFiles(params.getFiles());

		// 3. 파일 정보 저장 (to databjase)
		fileService.saveFiles(params.getId(), uploadFiles);

		// 4. 삭제할 파일 정보 조회 (from database)
		List<FileResponse> deleteFiles = fileService.findAllFileByIds(params.getRemoveFileIds());

		// 5. 파일 삭제 (from disk)
		fileUtils.deleteFiles(deleteFiles);

		// 6. 파일 삭제 (from database)
		fileService.deleteAllFileByIds(params.getRemoveFileIds());

		MessageDto message = new MessageDto("게시글 수정이 완료되었습니다.", "/post/list.do", RequestMethod.GET,
			queryParamsToMap(queryParams));
		return showMessageAndRedirect(message, model);
	}

	// 게시글 삭제
	// 전체 로직 - 게시글 번호(id)를 파라미터로 전달받아 특정 게시글을 삭제합니다.
	// 여기서 삭제는 테이블상에서의 물리적인 DELETE가 아닌, 삭제 여부 칼럼 상태 값을 변경 하는 논리삭제이다.
	// 로직 변경 - 기존에는 파라미터로 게시글 번호(id)만 수집, 이전 페이지 정보까지 수집하기 위해서는 SearchDto 타입의 객체인 queryParams를 파라미터로 선언
	// 추가적으로 message 객체를 생성할 때, 생성자의 마지막 파라미터에 처음 보는 메서드를 전달한다
	@PostMapping("/post/delete.do")
	public String deletePost(@RequestParam(value = "id") final Long id, final SearchDto queryParams, Model model) {
		postService.deletePost(id);
		MessageDto message = new MessageDto("게시글 삭제가 완료되었습니다.", "/post/list.do", RequestMethod.GET,
			queryParamsToMap(queryParams));
		return showMessageAndRedirect(message, model);
	}

	// wtf !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
}
