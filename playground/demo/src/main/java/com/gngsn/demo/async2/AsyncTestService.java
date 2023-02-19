package com.gngsn.demo.async2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public interface AsyncTestService {

    ResponseEntity<Object> excuteLongWorkProcess(int delaySecond);

    /**
     * 외부 API요청: 논블럭 방식(NIO)
     */
    void externalReqByNio();

    /**
     * 외부 API요청: 논블럭 방식(NIO) & 리턴처리
     */
    void externalReqByNioVer2(DeferredResult<Object> dr, long startMillis);

    /**
     * 외부 API요청: IO블럭킹 방식
     */
    ResponseEntity<String> externalReqByBlock();
}