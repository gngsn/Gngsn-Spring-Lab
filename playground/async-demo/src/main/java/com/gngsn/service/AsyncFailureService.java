package com.gngsn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncFailureService {
    final private Logger log = LoggerFactory.getLogger(AsyncFailureService.class);
    final private AsyncService asyncService;
    final private ApplicationContext applicationContext;


    public AsyncFailureService(AsyncService asyncService, ApplicationContext applicationContext) {
        this.asyncService = asyncService;
        this.applicationContext = applicationContext;
    }

    public void all() {
        /**
         * As step1() step2() step3() in same class as all().
         * which will make those 3 steps called by same caller as all() not the proxy caller. then async fail
         */
        step1();
        step2();
        step3();

        /**
         * But as step4() step5() step6() in different class. then 4,5,6 will do async way.
         */
        asyncService.step1();
        asyncService.step2();
        asyncService.step3();
    }

    public void allInSameClass() {
        /**
         *  Methods in the same class need to get the proxy object first, manually get the object
         */
        AsyncService bean = applicationContext.getBean(AsyncService.class);
        bean.step1();
        bean.step2();
        bean.step3();
    }

    @Async
    public void step1() {
        postpone(1_000);
        log.info("step 1: thread - " + Thread.currentThread());
    }

    @Async("asyncExecutor")
    public void step2() {
        postpone(3_000);
        log.info("step 2: thread - " + Thread.currentThread());
    }

    @Async("fixedThreadPool")
    public void step3() {
        postpone(2_000);
        log.info("step 3: thread - " + Thread.currentThread());
    }

    private void postpone(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            System.out.println("Error occurs during thread sleep");
        }
    }
}
