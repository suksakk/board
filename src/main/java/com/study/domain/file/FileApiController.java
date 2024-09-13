package com.study.domain.file;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.study.common.file.FileUtils;

import lombok.RequiredArgsConstructor;

// 댓글과 마찬가지로 파일도 REST API 방식으로 데이터만 주고 받는다,
// 사실 PostController에 FileService를 DI(의존성 주입) 해서 처리해도 되지만,
// 도메인 단위로 영역을 확실히 분리하기 위해서 파일용 컨트롤러를 따로 둔다
@RestController
@RequiredArgsConstructor
public class FileApiController {

	private final FileService fileService;
	private final FileUtils fileUtils;

	// 파일 리스트 조회
	@GetMapping("/posts/{postId}/files")
	public List<FileResponse> findAllFileByPostId(@PathVariable(value = "postId", required = false) final Long postId) {
		return fileService.findAllFileByPostId(postId);
	}

	// 첨부파일 다운로드
	// postId - 게시글 번호(PK)를 의미, fileId - 첨부파일 번호(PK)를 의미, file - DB에서 조회한 첨부파일 상세정보 의미,
	// resource - FileUtils의 readFileAsResourc()로 첨부파일 상세정보를 전달해서 다운로드할 첨부파일을 리소스 타입으로 조회한다
	@GetMapping("/posts/{postId}/files/{fileId}/download")
	public ResponseEntity<Resource> downloadFile(@PathVariable(value = "postId", required = false) final Long postId,
		@PathVariable(value = "fileId", required = false) final Long fileId) {

		FileResponse file = fileService.findFileById(fileId);
		Resource resource = fileUtils.readFileAsResource(file);
		try {
			// filename - 다운로드할 첨부파일의 이름을 의미, 리소스를 읽어들일 땐 실제로 디스크에 저장된 파일명(saveName)을 통해 접근했지만,
			// 다운로드되는 첨부파일의 이름은 원본 파일명(originalName)이 되어야 한다 추가적으로 URLEncoder.encode()를 이용해서 다운로드한 첨부파일의 이름이 깨지는걸 방지
			String filename = URLEncoder.encode(file.getOriginalName(), "UTF-8");
			// ResponseEntity - Rest API 방식에서 사용되는 클래스로, 이 클래스를 이용하면 사용자의 HTTP 요청(Request)에 대한 응답(Response) 데이터 개발자가 직접 제어가능
			// ResponseEntity에는 응답에 대한 상태코드, 헤더, 본문데이터 등을 설정 가능, 여기서는 파일의 MIME 타입, 파일명, 파일 크기, 마지막으로 resource를 응답 본문에 담아
			// 파일을 다운로드 처리 
			return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + filename + "\";")
				.header(HttpHeaders.CONTENT_LENGTH, file.getSize() + "")
				.body(resource);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("filename encoding faild : " + file.getOriginalName());
		}
	}

}
