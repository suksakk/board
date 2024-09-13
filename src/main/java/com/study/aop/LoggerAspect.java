package com.study.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

// AOP - 비즈니스 로직과는 별개로 공통 기능(로그 기록, 트랜잭션 처리, 보안검증 등)을 모듈화 하여 적용
// 적용 범위 - 특정 메서드나 클래스에서 호출되는 지점(join point)에서 세밀하게 적용 가능
// 기능 - 메서드 호출 전, 후 예외 발생 시 등 다양한 지점 동작
//     - 메서드 실행 시간 측정, 트랜잭션 관리, 예외처리 등 메서드와 관련된 상세한 정보 처리
// ex) 메서드 호출 전, 후 로그를 기록한다, 메서드 실행 시간을 측정하고 로그에 기록, 메서드 호출 중 예외 발생 시 예외를 처리하고 로그를 기록
// Advice 정의
// Before Advice - @Before - Target 메서드 호출 이전에 적용할 어드바이스 정의
// After Returning - @AfterReturning - Target 메서드가 성공적으로 실행되고, 결괏값을 반환한 뒤에 적용
// After Throwing - @AfterThrowing - Target 메서드에서 예외 발생 이후에 적용(try/catch의 catch와 유사)
// After - @After - Target 메서드에서 예외 발생 관계없이 적용 (try/catch의 finally와 유사)
// Around - @Around - Target 메서드 호출 이전과 이후 모두 적용 (가장 광범위하게 사용됨)

// @Component - 스프링 컨테이너에 빈(bean)으로 등록하기 위한 어노테이션
// @Bean은 개발자가 제어 할 수 없는 외부 라이브러리를 빈으로 등록할 때 사용
// @Compoent 개발자가 직접 정의한 클래스를 빈으로 등록할 때 사용
// @Aspect - AOP기능을 하는 클래스의 클래스 레벨에 선언하는 어노테이션
// @Around - 어드바이스의 종류 중 한 가지 어드바이스는 모두 다섯 가지의 타입이 있다
// 다섯 가지 중 어라운드는 메서드의 호출을 자체를 제어할 수 있기 때문에 어드바이스 중 가장 강력한 기능 담당
@Slf4j
@Aspect
@Component
public class LoggerAspect {

	// Around -> 안에서 execution으로 시작하는 구문은 포인트 컷을 지정하는 문법
	// 다른 명사로는 within과 bean이 있다 세 개 중 가장 많이 사용되는 명시자는
	// execution으로 접근제어자, 리턴타입, 타입 패턴, 메서드, 파라미터 타입, 예외 타입 등 조합해서 정교한 포인트 컷 생성가능

	// execution() 예시
	// PostResponse select*(..) -> 리턴 타입이 PostResponse타입이고, 메서드 이름이 select로 시작
	// 파라미터가 0개 이상인 모든 메서드가 호출될 때(0개 이상은 패키지, 메서드, 파라미터 등 모든 것 의미)
	// * com.study.controller.*() -> 해당 패키지 내의 파라미터가 없는 모든 메서드가 호출될 때
	// * com.study.controller.*(..) -> 해당 패키지 내의 파라미터가 0개 이상인 모든 메서드가 호출될 때
	// * com.study..select(*) -> com.study의 모든 하위 패키지에 존재하는 select로 시작하고, 파라미터가 한 개인 모든 메서드 호출 될 때
	// * com.study..select(*, *) ->  com.study의 모든 하위 패키지에 존재하는 select로 시작하고, 파라미터가 두 개인모든 메서드가 호출될 때
	// 포인트 컷 표현식 &&(and), ||(or)
	@Around("execution(* com.study.domain..*Controller.*(..)) || execution(* com.study.domain..*Service.*(..)) || execution(* com.study.domain..*Mapper.*(..))")
	public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

		String name = joinPoint.getSignature().getDeclaringTypeName();
		String type =
			StringUtils.contains(name, "Controller") ? "Controller ===> " :
				StringUtils.contains(name, "Service") ? "Service ===> " :
					StringUtils.contains(name, "Mapper") ? "Mapper ===> " :
						"";

		log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
		return joinPoint.proceed();
	}

	// 전체로직 - name에는 대상 파일의 경로(패키지 + 파일명) 저장, 결과적으로 printLog() signature 객체가 가진 정보를 이용,
	// 어떤 클래스의 어떤 메서드가 호출되었는지 로그 출력
	// 컨트롤러 : com.study.domain.post.PostController
	// 서비스 : com.study.domain.post.PostService
	// 매퍼 : com.study.domain.post.PostMapper

	// AOP 설정을 통해 어떤 클래스의 어떤 메서드가 실행되었는지 한눈에 확인 가능
	// 트랜잭션 - 하나의 작업에 여러 개의 작업이 같이 묶여 있는 개념으로 생각
	// ex) 1. A가 B의 은행은 선택하고, 계좌번호와 이체할 금액을 입력
	// 2. A의 계좌에서 50,000원이 출금
	// 3. B의 계좌에 50,000원이 입금 2, 3번의 시나리오처럼 함께 묶여 있는 작업을 트랜잭션
	// 4. A ===> B의 계좌 이체가 완료

	// AOP(Aspect Oriented Programming)
	// AOP -> 관점 지향 프로그래밍, OOP(Object Oriented Programming)을 더욱 더 OOP답게 사용할 수 있도록 도와주는 역할
	// AOP 여러 개의 핵심 비즈니스 로직 외에 공통으로 처리돼야 하는 로그 출력, 보안처리, 예외처리와 같은 코드를 별도로 분리해서
	// 하나의 단위로 묶는 모듈화의 개념으로 생각할 수 있다
	// AOP에서 관점은 핵심적인 관점과 부과적인 관점이 있다
	// 핵심적인 관점 - 핵심 비즈니스 로직을 의미, 부과적인 관점 - 공통으로 처리되어야할 코드를 의미
	// AOP를 적용하면 로그, 보안, 트랜잭션, 예외처리 같은 부가적인 기능들을 비즈니스 로직마다 일일이 추가하지 않아도 된다.

	// AOP 용어
	// Aspect - 공통적으로 적용될 기능을 의미, 부가적인 기능을 정의한 코드인 어드바이스와, 어드바이스를 어느 곳에 적용할지 결정하는 포인트컷의 조합으로 만든다.
	// Advice - 실제로 부가적인 기능을 구현한 객체를 의미합니다.
	// Join Point - 어드바이스를 적용할 위치를 의미, ex) PostService에서 CRUD를 처리하는 메서드중 원하는 메서드를 골라서 어드바이스를 적용가능
	// 이 때 PostService의 모든 메서드는 조인 포인트가 된다.
	// Pointcut - 어드바이스를 적용할 조인 포인트를 선별하는 과정이나 그 기능을 정의한 모듈을 의미한다. 정규표현식 이나 AspectJ 문법을 이용, 어떤 조인 포인트를 사용할 지 결정
	// Target - 실제로 비즈니스 로직을 수행하는 객체를 의미, 즉 어드바이스를 적용할 대상
	// Proxy - 어드바이스가 적용되었을 때 생성되는 객체를 의미
	// Introduction - 타겟에는 없는 새로운 메서드나 멤버(인스턴스) 변수를 추가하는 기능
	// Weaving - 포인트컷에 의해서 결정된 타겟의 조인 포인트에 어드바이스를 적용하는것을 의미
}
