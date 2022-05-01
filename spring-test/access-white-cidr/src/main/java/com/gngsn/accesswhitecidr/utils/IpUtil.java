package com.gngsn.accesswhitecidr.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.util.annotation.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class IpUtil {

    /**
     * Proxy, Caching server, Load Balancer 등을 거쳐올 경우
     * getRemoteAddr( ) 를 이용하여 IP 주소를 가지고 오지 못하기 때문에
     * Header에서 직접 가져와야 한다.
     */
    private static final String[] IP_HEADER_CANDIDATES = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

//    public static HttpServletRequest getCurrentRequest() {
//        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//    }

//    public static String getClientIp() {
//        return getClientIp(getCurrentRequest());
//    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = getIpXFF(request);

        for (String ipHeader: List.of(IP_HEADER_CANDIDATES)) {
            ip = request.getHeader(ipHeader);
            if (!notValidIp(ip)) return ip;
        }

        return ip != null ? ip : request.getRemoteAddr();
    }

    @Nullable
    private static String getIpXFF(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && isMultipleIpXFF(ip)) {
            ip = getClientIpWhenMultipleIpXFF(ip);
        }

        return ip;
    }

    private static boolean isMultipleIpXFF(String ip) {
        return ip.contains(",");
    }

    /**
     * X-Forwarded-For: <client>, <proxy1>, <proxy2> .. 형태일 경우
     *
     * @param ips      IP List (joined by a comma)
     * @return String  Single(Real) Client IP
     */
    private static String getClientIpWhenMultipleIpXFF(String ips) {
        return ips.split(",")[0];
    }


    private static boolean notValidIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
