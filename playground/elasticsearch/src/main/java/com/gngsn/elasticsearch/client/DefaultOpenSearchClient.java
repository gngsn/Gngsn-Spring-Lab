package com.gngsn.elasticsearch.client;


import co.elastic.clients.transport.TransportUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gngsn.elasticsearch.entity.RequestLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.bulk.BulkOperation;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.endpoints.BooleanResponse;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DefaultOpenSearchClient {


    private static final Logger LOG = LoggerFactory.getLogger(DefaultOpenSearchClient.class);

    private RestClient restClient;

    private OpenSearchTransport transport;

    private OpenSearchClient client;

    private final String INDEX = "aws-request-log";

    public DefaultOpenSearchClient(SearchClientProperty config) {
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(
            AuthScope.ANY, new UsernamePasswordCredentials(config.getUsername(), config.getPassword())
        );

        this.restClient = RestClient
            .builder(new HttpHost(config.getHost(), Integer.parseInt(config.getPort()), config.getSchema()))
            .setHttpClientConfigCallback(hc -> {
                    if (StringUtils.isNotBlank(config.getSslCertPath())) {
                        setSslCert(hc, config.getSslCertPath());
                    }
                    return hc.setDefaultCredentialsProvider(credsProv);
                }
            )
            .build();

        this.transport = new RestClientTransport(restClient, new JacksonJsonpMapper(new ObjectMapper().registerModule(new JavaTimeModule())));
        this.client = new OpenSearchClient(transport);
    }

    private void setSslCert(HttpAsyncClientBuilder hc, String sslCertPath) {
        try {
            hc.setSSLContext(TransportUtils.sslContextFromHttpCaCrt(new File(sslCertPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        try {
            client.bulk(e -> e.index(INDEX).operations(bulkOperations));
        } catch (Exception e) {
            LOG.error("elasticsearch store logs error", e);
        }
    }


    /**
     * create elasticsearch index.
     *
     * @param indexName index name
     */
    public void createIndex(final String indexName) {
        if (!existsIndex(INDEX)) {
            try {
                client.indices().create(c -> c.index(indexName));
            } catch (IOException e) {
                LOG.error("create index error");
            }
            LOG.info("create index success");
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
