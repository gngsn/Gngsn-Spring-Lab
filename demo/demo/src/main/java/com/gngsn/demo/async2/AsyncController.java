package com.gngsn.demo.async2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/async")
public class AsyncController {

    private Logger log = LoggerFactory.getLogger(com.gngsn.demo.async.AsyncController.class);
    private AsyncTestService asyncTestService;

    public AsyncController(AsyncTestService asyncTestService) {
        this.asyncTestService = asyncTestService;
    }

    /**
     * CASE 1. WebAsyncTask
     */
    @GetMapping("/delay/{sec}")
    public WebAsyncTask<ResponseEntity<Object>> delay(@PathVariable String sec) {

        log.info("Start: request async delay {}", sec);

        Callable<ResponseEntity<Object>> callable = () -> asyncTestService.excuteLongWorkProcess(Integer.parseInt(sec));
        return new WebAsyncTask<>(6_000L, "asyncThreadPoolTaskExecutor", callable);

    }

    /**
     * CASE 2. WebClient: Blocking
     */
    @GetMapping("/test/externalReqByBlock")
    public ResponseEntity<String> externalReqByBlock() {
        return asyncTestService.externalReqByBlock();
    }


    /**
     * CASE 3. WebClient: Non-Blocking
     */
    @GetMapping("/test/externalReqByNio")
    public ResponseEntity<String> externalReqByNio() {

        asyncTestService.externalReqByNio();
        return new ResponseEntity<>("externalReqByNio request " + LocalDateTime.now(), HttpStatus.OK);
    }

    /**
     * CASE 4. DeferredResult
     */
    @GetMapping("/test/externalReqByNio/ver2")
    public DeferredResult<Object> externalReqByNioVer2() {
        long startMillis = System.currentTimeMillis();
        DeferredResult<Object> dr = new DeferredResult<>();

        dr.onTimeout(() -> {
            log.error("#### Timeout error DeferredResult");
            ResponseEntity<String> timeoutError = ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.");
            dr.setErrorResult(timeoutError);
        });

        dr.onError((Throwable t) -> {

            log.error("#### onError  DeferredResult. {}", t.getMessage());
            ResponseEntity<String> serverError = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
            dr.setErrorResult(serverError);
        });

        asyncTestService.externalReqByNioVer2(dr, startMillis);
        return dr;
    }
}