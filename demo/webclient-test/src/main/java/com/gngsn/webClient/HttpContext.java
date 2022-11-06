package com.gngsn.webClient;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HttpContext implements Serializable {

    private static final long serialVersionUID = 8668695964617280718L;

    /**
     * is module data.
     */
    private String module;

    /**
     * is method name.
     */
    private String method;

    /**
     * httpMethod .
     */
    private String httpMethod;

    /**
     * this is sign .
     */
    private String sign;

    /**
     * timestamp .
     */
    private String timestamp;

    /**
     * appKey .
     */
    private String appKey;

    /**
     * path.
     */
    private String path;

    /**
     * the contextPath.
     */
    private String contextPath;

    /**
     * realUrl.
     */
    private String realUrl;

    /**
     * startDateTime.
     */
    private LocalDateTime startDateTime;

    public HttpContext(String module, String method, String httpMethod, String sign, String timestamp, String appKey, String path, String contextPath, String realUrl, LocalDateTime startDateTime) {
        this.module = module;
        this.method = method;
        this.httpMethod = httpMethod;
        this.sign = sign;
        this.timestamp = timestamp;
        this.appKey = appKey;
        this.path = path;
        this.contextPath = contextPath;
        this.realUrl = realUrl;
        this.startDateTime = startDateTime;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
}
