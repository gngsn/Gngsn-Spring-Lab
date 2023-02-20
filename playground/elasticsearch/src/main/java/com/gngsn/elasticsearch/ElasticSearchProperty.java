package com.gngsn.elasticsearch;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ElasticSearchProperty {
    private String host;
    private String port;
    private String username;
    private String password;
}