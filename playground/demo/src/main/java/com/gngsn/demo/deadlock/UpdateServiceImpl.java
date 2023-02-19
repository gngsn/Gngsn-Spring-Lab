package com.gngsn.demo.deadlock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateServiceImpl implements UpdateService {
    private final ReqTestDAO reqTestDAO;

    @Async
    @Override
    @Transactional
    public void asyncSaveTestDataSaveBulk(ReqDTO param) {

        try {
            reqTestDAO.deleteAlreadyExistTestList(param); //재 집계 감안하여 기존 존재 데이터 삭제

            LocalDateTime nowTS = LocalDateTime.now();
            param.setRegYmdt(nowTS);
            param.setModYmdt(nowTS);

            int insCnt = reqTestDAO.insertTestList(param);
            log.debug("Insert 결과: {}건의 데이터를 추가했습니다.", insCnt);

        } catch (Exception e) {
            log.error("Update (Delete -> Insert) 과정에서 오류 발생: errMsg: {}", e.getMessage());
            throw e;
        }
    }
}
