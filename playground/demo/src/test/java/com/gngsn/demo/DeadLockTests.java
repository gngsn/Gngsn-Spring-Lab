package com.gngsn.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.demo.deadlock.ReqDTO;
import com.gngsn.demo.deadlock.ReqDetailDTO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DeadLockTests {
    int REQ_CNT = 1000;
    int REQ_LIST_CNT = 500;

    static final String API_URI = "http://localhost:8080/test/save";
    public static final ObjectMapper OM = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static OkHttpClient CLIENT = new OkHttpClient.Builder().readTimeout(2, TimeUnit.SECONDS).connectTimeout(2, TimeUnit.SECONDS).writeTimeout(2, TimeUnit.SECONDS).build();

    @Test
    public void invokeDeadLock() throws JsonProcessingException, InterruptedException {
        final String svcCd = "GOOGLE";
        List<ReqDetailDTO> detailList = new ArrayList<>(REQ_LIST_CNT);

        for (int detailReqNo = 1; detailReqNo <= REQ_LIST_CNT; detailReqNo++) {
            detailList.add(
                new ReqDetailDTO(String.valueOf(detailReqNo), "test_name")
            );
        }

        for (int reqNo = 1; reqNo <= REQ_CNT; reqNo++) {

            ReqDTO reqDTO = new ReqDTO();
            reqDTO.setSvcCd(svcCd);
            final String reqId = String.valueOf((System.currentTimeMillis() % 1000) * (reqNo % 100));
            reqDTO.setReqId(reqId);
            reqDTO.setDetailList(detailList);

            executeApiReq(reqDTO);
            Thread.sleep(200);
        }
    }

    public void executeApiReq(ReqDTO reqDTO) throws JsonProcessingException, InterruptedException {
        final String paramJson = OM.registerModule(new JavaTimeModule()).writeValueAsString(reqDTO); //요청 pojo를 json String으로 변환

        RequestBody requestBody = RequestBody.create(paramJson, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(API_URI).post(requestBody).build();

        log.info("\n========= START: API로 데이터 전송 ========= \n\t- 요청 데이터 paramJson:{}", paramJson);

        try {
            CLIENT.newCall(request).enqueue(
                new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        log.debug("저장 요청 중 서버 연결에 실패하여 실행이 중지되었습니다. API 연결을 확인하세요. | error: {}", e.getMessage());
                        throw new RuntimeException("Connect Server Error. " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        log.info("response: {}, message: {}", response.toString(), response.message());

                        if (!response.isSuccessful()) {
                            String msg = String.format("\n\n****** Request Fail ********\nAPI 저장 요청에 실패했습니다. 데이터 확인 후 수정이 필요합니다.\nResponse:%s\nResponse Body:%s\n\n", response.toString(), response.body().string());
//                        response.close();
                            throw new RuntimeException(msg);
                        }
                        log.debug("\n\n\n****** Request Success ********\nResponseÏ:{}\nResponse Body:{}\n\n", response.toString(), response.body().string());
//                    response.close();
                    }
                });
        } catch (Exception e) {
            throw new RuntimeException(String.format("API 전송 중 서버 통신 오류 | errMsg: %s, err: %s", e.getMessage(), e));
        }

        log.info("\n\n========= END: API 로 데이터를 전송 완료 ========= ");
    }
}