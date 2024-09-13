package com.study.common.file;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.study.domain.file.FileRequest;
import com.study.domain.file.FileResponse;

// 페이징과 마찬가지로 파일 업로드/다운로드도 모든 영역에서 공통으로 사용할 수 있다
// 이 클래스는 디스크에 디렉터리(폴더)를 생성하거나, 파일을 업로드 또는 삭제하는 용도로 사용되는 클래스다.
// @Component - @Bean은 개발자가 컨트롤 할 수 없는 외부 라이브러리를 빈으로 등록할 때 사용하고, @Component는 개발자가 직접 정의한 클래스를 빈으로 등록할 때 사용
@Component
public class FileUtils {

	// uploadPath - 물리적으로 파일을 저장할 위치를 의미, 경로는 내가 원하는 곳으로 선언
	// 보통 OS별 디렉터리 경로를 구분할 때 File.separator를 이용한다
	// Paths.get()을 이용하면 OS에 상관없이 디렉터리 경로를 구분할 수 있다
	private final String uploadPath = Paths.get("/Users/gm/desktop/img/")
		.toString();

	/**
	 * 다중 파일 업로드
	 * @param multipartFiles - 파일 객체 List
	 * @return DB에 저장할 파일 정보 List
	 */
	// uploadFiles() - 스프링은 파일 업로드를 쉽게 처리할 수 있도록 MultipartFile 인터페이스를 제공해 준다
	// 사용자가 화면에서 파일을 업로드 한 후 폼을 전송하면, MultipartFile 객체에 사용자가 업로드한 파일 정보가 담긴다.
	// 이 메서드의 포인트는 변수 files에 uploadFile()의 결괏값을 담아 리턴하는 것
	public List<FileRequest> uploadFiles(final List<MultipartFile> multipartFiles) {
		List<FileRequest> files = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (multipartFile.isEmpty()) {
				continue;
			}
			files.add(uploadFile(multipartFile));
		}
		return files;
	}

	/**
	 * 단일 파일 업로드
	 * @param multipartFile - 파일 객체
	 * @return DB에 저장할 파일 정보
	 */
	// uploadFile() - 단일 파일을 디스크에 업로드 한다 MultipartFile의 isEmpty()는 파일의 유무를 체크하는 함수이다
	// 업로드 된 파일이 없는 경우에는 null을 리턴해서 로직을 종료한다
	// 메인 로직의 각 변수는 디스크에 저장할 파일명(saveName), 오늘날짜(today), 파일업로드의 경로(uploadPath(디렉토리 + 파일명)), 업로드할 파일 객체(uploadFile)를 의미
	// 파일은 uploadPath에 해당되는 경로에 생성되며, MultipartFile의 transferTo()가 정상적으로 실행되면 파일 생성(write)이 완료
	// 결과적으로 메서드가 리턴하는 객체에는 디스크에 생성된 파일 정보가 담기게 된다.
	public FileRequest uploadFile(final MultipartFile multipartFile) {

		if (multipartFile.isEmpty()) {
			return null;
		}

		String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
		String today = LocalDate.now()
			.format(DateTimeFormatter.ofPattern("yyMMdd"))
			.toString();
		String uploadPath = getUploadPath(today) + File.separator + saveName;
		File uploadFile = new File(uploadPath);

		try {
			multipartFile.transferTo(uploadFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return FileRequest.builder()
			.originalName(multipartFile.getOriginalFilename())
			.saveName(saveName)
			.size(multipartFile.getSize())
			.build();
	}

	/**
	 * 저장 파일명 생성
	 * @param filename 원본 파일명
	 * @return 디스크에 저장할 파일명
	 */
	// generateSaveFilename() - uploadFile()의 변수 saveName에서 호출하는 메서드, 변수 uuid에는 32자리 랜덤 문자열을,
	// extension에는 업로드 한 파일의 확장자를 담아(랜덤 문자열 + ".: + 파일 확장자)에 해당되는 파일명을 리턴한다. 이 파일명은 실제로 디스크에 생성되는 파일명을 의미
	private String generateSaveFilename(final String filename) {
		String uuid = UUID.randomUUID()
			.toString()
			.replaceAll("-", "");

		String extension = StringUtils.getFilenameExtension(filename);
		return uuid + "." + extension;
	}

	/**
	 * 업로드 경로 반환
	 * @return 업로드 경로
	 */
	// getUploadPath() - 변수 uploadPath에 해당되는 경로를 리턴한다. addPath 파라미터가 선언된 getUploadPath()는 uploadFile()의 변수 uploadPath에서 호출하는 메서드로,
	// 당장은 기본 업로드 경로에 오늘 날짜(today)를 연결하는 용도로 사용
	private String getUploadPath() {

		return makeDirectories(uploadPath);
	}

	/**
	 * 업로드 경로 반환
	 * @param addPath - 추가 경로
	 * @return 업로드 경로
	 */
	private String getUploadPath(final String addPath) {
		return makeDirectories(uploadPath + File.separator + addPath);
	}

	/**
	 * 업로드 폴더(디렉터리) 생성
	 * @param path - 업로드 경로
	 * @return 업로드 경로
	 */
	// makeDirectories() - getUploadPath()에서 호출하는 메서드. 디스크에 경로(path)에 해당되는 디렉터리가 없으면, path에 해당되는 모든 경로에 폴더를 생성
	// ex) - C:\a\b\c\d\e 라고 가정 시 a~e까지의 모든 경로가 폴더로 생성된다.
	private String makeDirectories(final String path) {
		File dir = new File(path);
		if (dir.exists() == false) {
			dir.mkdirs();
		}
		return dir.getPath();
	}

	/**
	 * 파일 삭제 (from Disk)
	 * @param files - 삭제할 파일 정보 List
	 */// deleteFiles() - DB에서 조회한 삭제할 파일 정보를 전달받아 디스크에서 파일을 삭제 처리한다
	// 변수 uploadedDate는 DB에 파일 정보가 저장된 시간(created_date)을 '연월일(yyMMdd)'형식으로 포맷한 값을 의미
	public void deleteFiles(final List<FileResponse> files) {
		if (CollectionUtils.isEmpty(files)) {
			return;
		}
		for (FileResponse file : files) {
			String uploadedDate = file.getCreatedDate()
				.toLocalDate()
				.format(DateTimeFormatter.ofPattern("yyMMdd"));
			deleteFile(uploadedDate, file.getSaveName());
		}
	}

	/**
	 * 파일 삭제 (from Disk)
	 * @param addPath  - 추가 경로
	 * @param filename - 파일명
	 */
	// deleteFile() - 추가 경로와 디스크에 저장된 파일명을 기준으로 파일을 삭제 처리 한다
	// addPath는 업로드 연월일 폴더를 처리하기 위해 사용되는 파라미터로, deleteFiles()의 uploadedDate를 의미한다
	// FileUtils 클래스의 uploadFile()은 파일을 업로드 하는 시점에 오늘 날짜를 기준으로 연월일 폴더를 생성하고,
	// 생성된 연월일 폴더에 파일을 Write 하기 때문에 addPath는 필수 파라미터가 된다.
	// filename은 디스크에 저장된 파일명을 의미한다 이 또한 파일 테이블의 save_name을 통해 알 수 있다.
	private void deleteFile(final String addPath, final String filename) {
		String filePath = Paths.get(uploadPath, addPath, filename).toString();
		deleteFile(filePath);
	}

	/**
	 * 파일 삭제 (from Disk)
	 * @param filePath - 파일 경로
	 */
	// 파일의 전체 경로(디렉터리 경로 + 파일명)를 전달받아 파일을 삭제 처리한다.
	// file.exist()로 파일이 존재하는지 확인 후 file.delete()로 물리적 파일을 디스크에서 완전히 삭제
	private void deleteFile(final String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 다운로드할 첨부파일(리소스) 조회(as Resource)
	 * @param file - 첨부파일 상세 정보
	 * @return 첨부파일(리소스)
	 */
	// file - DB조회한 첨부파일의 상세정보를 의미한다. 이를 통해 업로드된 첨부파일의 경로를 찾아낼 수 있다.
	public Resource readFileAsResource(final FileResponse file) {
		// 첨부파일이 업로드된 날짜를 의미한다. file 객체를 담긴 첨부파일의 생성일시(creadtedDate)를 연월일("yyMMdd")형태로 포맷하면,
		// 첨부파일이 업로드된 날짜를 추적할 수 있다
		String uploadedDate = file.getCreatedDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyMMdd"));
		// filename - 디스크에 업로드된 파일명을 의미
		String filename = file.getSaveName();
		// filePath - 디스크에 업로드된 파일의 전체 경로를 의미 (파일경로 + 파일명)
		Path filePath = Paths.get(uploadPath, uploadedDate, filename);
		try {
			// resource - 본 메서드에서 핵심이 되는 객체 Resource의 구현체인 UriResource의 생성자 파라미터로 filePath에 담긴 첨부파일의 경로를
			// 전달해서 Resource 객체를 생성 만일 리소스가 없거나, 리소스 타입의 파일이 아닌 경우에는 예외를 던져 로직을 종료
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() == false || resource.isFile() == false) {
				throw new RuntimeException("file not found : " + filePath.toString());
			}
			return resource;
		} catch (MalformedURLException e) {
			throw new RuntimeException("file not found : " + filePath.toString());
		}
	}
}
