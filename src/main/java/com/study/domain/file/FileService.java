package com.study.domain.file;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileMapper fileMapper;

	// saveFiles() - 게시글 번호(postId)와 파일 정보(files)를 전달받아, 업로드된 파일 정보를 테이블에 저장하는 역할
	// 만약 게시글을 저장(INSERT 또는 UPDATE)하는 시점에 업로드된 파일이 없다면 로직을 종료하고,
	// 파일이 있으면 모든 요청 객체에 게시글 번호(postId)를 세팅한 후 테이블에 파일정보를 저장
	@jakarta.transaction.Transactional
	public void saveFiles(final Long postId, final List<FileRequest> files) {
		if (CollectionUtils.isEmpty(files)) {
			return;
		}
		for (FileRequest file : files) {
			file.setPostId(postId);
		}
		fileMapper.saveAll(files);
	}

	/**
	 * 파일 리스트 조회
	 *
	 * @param postId - 게시글 번호 (FK)
	 * @return 파일 리스트
	 */
	public List<FileResponse> findAllFileByPostId(final Long postId) {
		return fileMapper.findAllByPostId(postId);
	}

	/**
	 * 파일 리스트 조회
	 *
	 * @param ids - PK 리스트
	 * @return 파일 리스트
	 */
	public List<FileResponse> findAllFileByIds(final List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		return fileMapper.findAllByIds(ids);
	}

	/**
	 * 파일 삭제 (from Database)
	 *
	 * @param ids - PK 리스트
	 */
	@Transactional
	public void deleteAllFileByIds(final List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		fileMapper.deleteAllByIds(ids);
	}

	/**
	 * 파일 상세정보 조회
	 *
	 * @param id - PK
	 * @return 파일 상세정보보     */
	public FileResponse findFileById(final Long id) {
		return fileMapper.findById(id);
	}
}
