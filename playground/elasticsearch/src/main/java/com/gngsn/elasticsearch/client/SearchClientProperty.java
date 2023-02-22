package com.gngsn.elasticsearch.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
public class SearchClientProperty {
    private String schema;

    @NonNull
    private String host;

    @NonNull
    private String port;

    private String username;
    private String password;

    private String sslCertPath;
}