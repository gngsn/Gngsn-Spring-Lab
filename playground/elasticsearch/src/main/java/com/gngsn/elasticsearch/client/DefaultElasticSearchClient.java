package com.gngsn.elasticsearch.client;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.elasticsearch.common.RequestLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultElasticSearchClient {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultElasticSearchClient.class);

    private RestClient restClient;

    private ElasticsearchTransport transport;

    private ElasticsearchClient client;

    private final String INDEX = "local-request-log";

    /**
     * constructor
     *
     * @param config elasticsearch client config
     */
    public DefaultElasticSearchClient(SearchClientProperty config) {
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
            AuthScope.ANY, new UsernamePasswordCredentials(config.getUsername(), config.getPassword())
        );

        restClient = RestClient
            .builder(new HttpHost(config.getHost(), Integer.parseInt(config.getPort()), config.getSchema()))
            .setHttpClientConfigCallback(hc -> {
                if (StringUtils.isNotBlank(config.getSslCertPath())) {
                    try {
                        File certFile = new File(config.getSslCertPath());
                        SSLContext sslContext = TransportUtils.sslContextFromHttpCaCrt(certFile);
                        hc.setSSLContext(sslContext);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return hc.setDefaultCredentialsProvider(credsProv);
            })
            .build();

        transport = new RestClientTransport(restClient, new JacksonJsonpMapper(new ObjectMapper().registerModule(new JavaTimeModule())));
        client = new ElasticsearchClient(transport);

        LOG.info("init ElasticSearchLogCollectClient success");
    }

    /**
     * init elasticsearch client.
     */
    public void initClient() {
        if (!existsIndex(INDEX)) {
            createIndex(INDEX);
            LOG.info("create index success");
        }
    }

    public void consume(final List<RequestLog> logs) {
        List<BulkOperation> bulkOperations = new ArrayList<>();
        logs.forEach(log -> {
            try {
                bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(log).index(INDEX)).build());
            } catch (Exception e) {
                LOG.error("add logs error", e);
            }
        });
        // Bulk storage
        try {
            client.bulk(e -> e.index(INDEX).operations(bulkOperations));
        } catch (Exception e) {
            LOG.error("elasticsearch store logs error", e);
        }
    }

    /**
     * determine whether the index already exists.
     *
     * @param indexName index name
     * @return true or false
     */
    public boolean existsIndex(final String indexName) {
        try {
            BooleanResponse existsResponse = client.indices().exists(c -> c.index(indexName));
            return existsResponse.value();
        } catch (Exception e) {
            LOG.error("fail to check the index exists");
        }
        return true;
    }

    /**
     * create elasticsearch index.
     *
     * @param indexName index name
     */
    public void createIndex(final String indexName) {
        try {
            client.indices().create(c -> c.index(indexName));
        } catch (IOException e) {
            LOG.error("create index error");
        }
    }

    /**
     * close client.
     */
    public void close() {
        if (Objects.nonNull(restClient)) {
            try {
                transport.close();
            } catch (IOException e) {
                LOG.error("transport close has IOException : ", e);
            }
            try {
                restClient.close();
            } catch (IOException e) {
                LOG.error("restClient close has IOException : ", e);
            }
        }
    }
}
