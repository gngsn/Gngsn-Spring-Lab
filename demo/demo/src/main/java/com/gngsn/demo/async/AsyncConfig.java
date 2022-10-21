package com.gngsn.demo.async;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public FilterRegistrationBean<ThreadPrintFilter> registrationBean() {
        FilterRegistrationBean<ThreadPrintFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new ThreadPrintFilter());
        return filter;
    }

    @Bean(name = "async-test")
    public ThreadPoolTaskExecutor mvcAsyncTaskExecutor() {
        ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor(); //참고: https://keichee.tistory.com/m/382

        //te.setPrestartAllCoreThreads(true); //어플리케이션 시작될 때 CorePoolSize만큼 스레드를 미리 시작(배포 직후 부하가 많이 걸릴 경우를 대비)

        te.setCorePoolSize(100); //동시 실행시킬 수 있는 스레드 갯수(AllowCoreThreadTimeOut 값이 false이면, 생성된 후 해당 갯수만큼 스레드는 유지됨)
        te.setQueueCapacity(100); //쓰레드 풀 큐의 사이즈. corePoolSize 개수를 넘어설 때 큐에 해당 task들이 쌓이게 됨.최대로 maxPoolSize 개수 만큼 쌓일 수 있음
        te.setMaxPoolSize(500); //스레드풀의 최대 사이즈

        //어플리케이션 성격에 맞게 설정 수정
        te.setAllowCoreThreadTimeOut(true); //쓰레드가 유휴상태로 있을 때, keepAliveTime 만큼 기다렸다가 죽을지 여부
        te.setKeepAliveSeconds(60); //스레드풀의 스레드가 죽을 때까지의 대기시간

        te.setThreadNamePrefix("async-test");

        te.initialize();

        return te;
    }
}