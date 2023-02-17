package com.gngsn.springcore.aop;

import java.lang.annotation.*;


// RetentionPolicy - 이 Annotation 정보를 어디까지 유지할 것인가
// .CLASS -> 클래스 파일까지만 정의하겠다 (default가 class)
@Retention(RetentionPolicy.CLASS)
@Documented
@Target(ElementType.METHOD)
public @interface PerfLogging {

}
