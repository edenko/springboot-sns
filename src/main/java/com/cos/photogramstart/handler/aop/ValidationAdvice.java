package com.cos.photogramstart.handler.aop;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component // RestController, Service 등 모든 것들이 Component를 상속함
@Aspect
public class ValidationAdvice {

//  @Before() // 함수 실행 직전에 실행
//  @After() // 함수 끝나고 실행
  @Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") // 함수 시작 ~ 끝까지 관여할 때
  // excution(접근지정자 패키지.controller로 끝나는 모든 애들.모든 메서드.(메서드의 파라미터))
  public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    // proceedingJoinPoint : 함수 내부(매개변수)에 접근 할 수 있음, 낚아채서 여기 내부가 먼저 실행
    Object[] args = proceedingJoinPoint.getArgs();
    for(Object arg : args) {
      if(arg instanceof BindingResult) {
        BindingResult bindingResult = (BindingResult) arg;
        if(bindingResult.hasErrors()) {
          Map<String, String> errorMap = new HashMap<>();
          for(FieldError error:bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
          }
          throw new CustomValidationApiException("유효성 검사 실패함", errorMap);
        }
      }
    }
    return proceedingJoinPoint.proceed(); // 다시 그 함수로 돌아가서 함수 실행
  }

  @Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
  public Object Advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object[] args = proceedingJoinPoint.getArgs();
    for(Object arg : args) {
      if(arg instanceof BindingResult) {
        System.out.println("유효성 검사를 하는 함수입니다.");
        BindingResult bindingResult = (BindingResult) arg;
        if(bindingResult.hasErrors()) {
          Map<String, String> errorMap = new HashMap<>();
          for (FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
          }
          throw new CustomValidationException("유효성 검사 실패함", errorMap);
        }
      }
    }
    return proceedingJoinPoint.proceed();
  }

}
