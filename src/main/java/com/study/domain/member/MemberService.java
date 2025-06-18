package com.study.domain.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	// passwordEncoder - SecurityConfig에 선언한 PasswordEncoder 빈(bean)이다
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 로그인
	 * @param loginId  - 로그인 ID
	 * @param password - 비밀번호
	 * @return 회원 상세정보
	 */
	public MemberResponse login(final String loginId, final String password) {

		// 1. 회원 정보 및 비밀번호 조회
		// 로그인 페이지에 입력한 아이디와 비밀번호를 전달받아 회원 정보를 조히
		// encodePassword는 회원 테이블에 암호화된 비밀번호이다. member가 null인 경우에
		// member.getPassword()를 실행하면 NPE가 발생하기 때문에 빈 문자열("")로 처리
		MemberResponse member = findMemberByLoginId(loginId);
		String encodedPassword = (member == null) ? "" : member.getPassword();
		// 2. 회원 정보 및 비밀번호 체크
		// passwordEncoder의 matches()로 사용자가 입력한 비밀번호(password)와 회원 테이블에 암호화된 비밀번호(encodePassword)를 비교
		// 두 조건 모두 false인 경우가 정상적인 케이스
		if (member == null || passwordEncoder.matches(password, encodedPassword) == false) {
			return null;
		}

		// 3. 회원 응답 객체에서 비밀번호를 제거한 후 회원 정보 리턴
		// member.clearPassword()로 암호화된 회원의 비밀번호를 초기화("")한 후 회원 응답 객체를 리턴
		member.clearPassword();
		return member;
	}

	/**
	 * 회원 정보 저장 (회원가입)
	 * @param params - 회원 정보
	 * @return PK
	 */
	// saveMember() - 회원정보를 저장한다. MemberRequest의 encodingPassword()를 호출해서 비밀번호를 암호화 한다
	// PasswordEncoder의 encode()는 파라미터로 전달한 값을 60 자리의 난수로 리턴해 준다. 회원 테이블의 password를 varchar(60) 자리로 선언한 이유도 이와 같다
	@jakarta.transaction.Transactional
	public Long saveMember(final MemberRequest params) {
		params.encodingPassword(passwordEncoder);
		memberMapper.save(params);
		return params.getId();
	}

	/**
	 * 회원 상세정보 조회
	 * @param loginId - UK
	 * @return 회원 상세정보
	 */
	// findMemberByLoginId() - 로그인 ID를 기준으로 회원 상세정보를 조회한다.
	public MemberResponse findMemberByLoginId(final String loginId) {
		return memberMapper.findByLoginId(loginId);
	}

	/**
	 * 회원 정보 수정
	 * @param params - 회원 정보
	 * @return PK
	 */
	// updateMember() 회원 정보를 수정한다. saveMember()와 실행되는 쿼리에만 차이가 있다.
	// @Transactional - 회원 정보에 있어서 수정을 할 때 트랜잭션의 원자성을 보장하기 위해 @Transactional 어노테이션을 사용한다
	@jakarta.transaction.Transactional
	public Long updateMember(final MemberRequest params) {
		params.encodingPassword(passwordEncoder);
		memberMapper.update(params);
		return params.getId();
	}

	/**
	 * 회원 정보 삭제 (회원 탈퇴)
	 * @param id - PK
	 * @return PK
	 */
	@Transactional
	// deleteMemberById() - PK 기준으로 회원 정보를 삭제한다. 게시글, 댓글과 마찬가지로 논리 삭제 방식을 이용한다
	public Long deleteMemberById(final Long id) {
		memberMapper.deleteById(id);
		return id;
	}

	/**
	 * 회원 수 카운팅 (ID 중복 체크)
	 * @param loginId - UK
	 * @return 회원 수
	 */
	// countMemberByLoginId() - 로그인 ID를 기준으로 회원 수를 카운팅한다. 아이디 중복을 체크하는 용도로 사용
	public int countMemberByLoginId(final String loginId) {
		return memberMapper.countByLoginId(loginId);
	}

}
