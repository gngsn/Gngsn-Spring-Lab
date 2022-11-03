package com.gngsn.webClient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
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

}
