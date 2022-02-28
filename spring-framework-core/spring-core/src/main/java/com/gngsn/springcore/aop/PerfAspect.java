package com.gngsn.springcore.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerfAspect {
    // 2가지 정보가 필요 -> 해야할 일(advice), 어디에 적용할 것인가(pointcut)

//    logPerf 라는 advice를 어디에 적용할 것인가 -> pointcut.
//    @Around("execution(* com.gngsn.springcore..*.EventService.*(..))")
//    @Around("@annotation(PerfLogging)")
    @Around("bean(simpleEventService)")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        // method invocation과 비슷한 개념. 메서드를 감싸고 있다.
        Object retVal = pjp.proceed();
        System.out.println(System.currentTimeMillis() - begin);
        return retVal;
    }

    @Before("bean(simpleEventService)")
    public void hello() {
        System.out.println("hello");
    }

}
