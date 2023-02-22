package com.gngsn.elasticsearch;

import com.gngsn.elasticsearch.client.SearchClientProperty;
import com.gngsn.elasticsearch.entity.RequestLog;
import com.gngsn.elasticsearch.client.DefaultOpenSearchClient;
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
class DefaultOpenSearchClientTest {
    DefaultOpenSearchClient defaultOpenSearchClient;

    @Value("${aws.opensearch.domain}")
    private String host;
    @Value("${aws.opensearch.user}")
    private String user;
    @Value("${aws.opensearch.password}")
    private String password;

    @BeforeEach
    public void setUp() {
        SearchClientProperty property = new SearchClientProperty("https", host, "-1", user, password, null);
        defaultOpenSearchClient = new DefaultOpenSearchClient(property);
    }

    @Test
    public void testConsume() {
        List<RequestLog> logs = new ArrayList<>();

        RequestLog requestLog = new RequestLog();
        requestLog.setTraceId(String.valueOf(UUID.randomUUID()));
        requestLog.setTimeLocal(LocalDateTime.now().toString());
        requestLog.setClientIp("11.22.33.44");
        requestLog.setHost("192.168.0.1");
        requestLog.setQueryParams("/es-log/test");
        logs.add(requestLog);

        assertDoesNotThrow(() -> defaultOpenSearchClient.consume(logs));
        defaultOpenSearchClient.close();

        logs.forEach(l -> log.info(l.toString()));
    }

    @Test
    public void testCreateIndex() {
        defaultOpenSearchClient.createIndex("test");
    }

    @AfterEach
    public void close() {
        defaultOpenSearchClient.close();
    }

}