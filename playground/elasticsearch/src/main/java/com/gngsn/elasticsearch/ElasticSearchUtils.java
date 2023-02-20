package com.gngsn.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.gngsn.RequestLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ElasticSearchUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchUtils.class);

    private RestClient restClient;

    private ElasticsearchTransport transport;

    private ElasticsearchClient client;

    private final String INDEX = "requestLog";


    /**
     * init elasticsearch client.
     *
     * @param config elasticsearch client config
     */
    public void initClient(final ElasticSearchProperty config) {
        RestClientBuilder builder = RestClient.builder(new HttpHost(config.getHost(), Integer.parseInt(config.getPort()), "https"));

        if (!StringUtils.isBlank(config.getUsername())) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(config.getUsername(), config.getPassword()));
            builder.setHttpClientConfigCallback(asyncClientBuilder -> {
//            asyncClientBuilder.disableAuthCaching();
                return asyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }

        restClient = builder.build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
        LOG.info("init ElasticSearchLogCollectClient success");

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
