package com.gngsn.elasticsearch;

import com.gngsn.elasticsearch.common.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringBootTest
class ElasticSearchUtilsTest {

    ElasticSearchProperty property;
    ElasticSearchUtils elasticSearchClient;

    List<RequestLog> logs = new ArrayList<>();

    @Value("${local.elaticsearch.host}")
    private String host;

    @Value("${local.elaticsearch.user}")
    private String user;

    @Value("${local.elaticsearch.password}")
    private String password;

    @Value("${aws.opensearch.sslCertPath}")
    private String sslCertPath;

    @BeforeEach
    public void setUp() {
        RequestLog requestLog = new RequestLog();
        requestLog.setClientIp("11.22.33.44");
        requestLog.setHost("192.168.0.1");
        requestLog.setQueryParams("/es-log/test");
        logs.add(requestLog);

        property = new ElasticSearchProperty(host, "9200", user, password, sslCertPath);
        elasticSearchClient = new ElasticSearchUtils();
    }

    @Test
    public void testConsume() throws Exception {
        elasticSearchClient.initClient(property);
        String msg = "";

        try {
            elasticSearchClient.consume(logs);
        } catch (Exception e) {
            msg = "false";
        }

        Assertions.assertEquals(msg, "");
        Thread.sleep(2000);
        elasticSearchClient.close();
    }

    @AfterEach
    public void close() {
        elasticSearchClient.close();
    }

    @Test
    public void testCreateIndex() {
        elasticSearchClient.createIndex("test");
    }
}