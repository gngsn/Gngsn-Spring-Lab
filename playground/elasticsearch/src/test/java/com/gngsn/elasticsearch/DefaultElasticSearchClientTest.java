package com.gngsn.elasticsearch;

import com.gngsn.elasticsearch.common.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@Slf4j
@SpringBootTest
class DefaultElasticSearchClientTest {
    DefaultElasticSearchClient elasticSearchClient;


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
        SearchClientProperty property = new SearchClientProperty("https", host, "9200", user, password, sslCertPath);
        elasticSearchClient = new DefaultElasticSearchClient(property);
    }

    @Test
    public void testConsume() {
        List<RequestLog> logs = new ArrayList<>();
        elasticSearchClient.initClient();

        RequestLog requestLog = new RequestLog();
        requestLog.setTraceId(String.valueOf(UUID.randomUUID()));
        requestLog.setTimeLocal(LocalDateTime.now().toString());
        requestLog.setClientIp("11.22.33.44");
        requestLog.setHost("192.168.0.1");
        requestLog.setQueryParams("/es-log/test");
        logs.add(requestLog);

        assertDoesNotThrow(() -> elasticSearchClient.consume(logs));

        elasticSearchClient.close();
        logs.forEach(l -> log.info(l.toString()));
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