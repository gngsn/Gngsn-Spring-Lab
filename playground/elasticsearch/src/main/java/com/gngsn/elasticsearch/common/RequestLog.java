package com.gngsn.elasticsearch.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestLog {

    private String clientIp;
    private String timeLocal;
    private String method;
    private String requestHeader;
    private String responseHeader;
    private String queryParams;
    private String requestBody;
    private String requestUri;
    private String responseBody;
    private Integer responseContentLength;
    private String rpcType;
    private Integer status;
    private String upstreamIp;
    private Long upstreamResponseTime;
    private String userAgent;
    private String host;
    private String module;
    private String traceId;
    private String path;

    @Override
    public String toString() {
        return "RequestLog{"
            + "timeLocal='" + timeLocal + '\''
            + ", clientIp='" + clientIp + '\''
            + ", method=" + method
            + ", requestHeader=" + requestHeader
            + ", responseHeader=" + responseHeader
            + ", queryParams=" + queryParams
            + ", requestBody=" + requestBody
            + ", requestUri=" + requestUri
            + ", responseBody=" + responseBody
            + ", responseContentLength=" + responseContentLength
            + ", rpcType=" + rpcType
            + ", status=" + status
            + ", upstreamIp=" + upstreamIp
            + ", upstreamResponseTime=" + upstreamResponseTime
            + ", userAgent=" + userAgent
            + ", host=" + host
            + ", module=" + module
            + ", traceId=" + traceId
            + ", path=" + path
            + '}';
    }
}