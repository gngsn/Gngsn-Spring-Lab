package com.gngsn.demo.deadlock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/test/save")
@RequiredArgsConstructor
public class ReqTestController {

    private final UpdateService testService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveTestDataBulk(@Valid @RequestBody ReqDTO param) {

        try {
            log.info("[TEST API] Request param: {}", param);

            testService.asyncSaveTestDataSaveBulk(param); //데이터 저장
            return new ResponseEntity<>("Request success(async)", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception msg:{} | req param:{} | stack trace:", e.getMessage(), param, e);
            return new ResponseEntity<>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}