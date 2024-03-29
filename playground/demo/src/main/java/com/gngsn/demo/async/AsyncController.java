package com.gngsn.demo.async;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
public class AsyncController {

    public static final Logger log = LoggerFactory.getLogger(AsyncController.class);

//    public static void main(String[] args) {
//        SpringApplication.run(AsyncApplication.class, args);
//    }

    @SneakyThrows
    @GetMapping("/receive")
    public String receive(@RequestParam("data") String data) {
        log.debug("### [{}] called /receive", Thread.currentThread().getName());
        Thread.sleep(3000);

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " [async] request ip : " + getCurrentRequest().getRequestURI());
        });


        log.debug("### [{}] data : {} | thread local : {} | request ip  : {}", Thread.currentThread().getName(), data, ThreadPrintFilter.threadLocal.get(), getCurrentRequest().getRequestURI());
        return "received data : " + data;
    }

    @SneakyThrows
    @GetMapping("/async/receive")
    public WebAsyncTask<String> asyncReceive(@RequestParam("data") String data) {
        log.debug("### [{}] called /async/receive", Thread.currentThread().getName());
        Thread.sleep(1500);
//            ExecutorService executorService = Executors.newFixedThreadPool(1);
//            executorService.submit(() -> {
//                System.out.println(Thread.currentThread().getName() + " [async] request ip : " + getCurrentRequest().getRequestURI());
//            });

        return new WebAsyncTask<>(5_000L, "async-test", () -> {
            log.debug("### [{}] data : {} | thread local : {} | request ip : {}", Thread.currentThread().getName(), data, ThreadPrintFilter.threadLocal.get(), getCurrentRequest().getRequestURI());
            Thread.sleep(1500);
            return "received data : " + data;
        });
    }

    /**
     * 서블릿 request를 얻음
     *
     * @return
     */
    public static HttpServletRequest getCurrentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}