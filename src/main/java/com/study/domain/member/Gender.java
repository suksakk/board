package com.study.domain.member;

// 회원 테이블의 gender에는 'M' 과 'F' 둘 중 하나의 값만 저장할 수 있다
// java에서도 Enum 클래스를 이용해서 상수를 처리할 수 있다.
// Enum은 Enumeration 약자이다. - 열거, 목록, 알림표 라는 뜻 열거형이란 뜻으로 사용 멤버라 볼리는 명명된 값의 집합을 이루는 자료형
// 즉 상수 데이터들의 집합으로 치부 - 한정된 데이터 묶음을 열거형 타입인 Enum으로 묶는것 (ex - 주사위 숫자, 남여, 계절, 요일)  
public enum Gender {

	M, F

}
