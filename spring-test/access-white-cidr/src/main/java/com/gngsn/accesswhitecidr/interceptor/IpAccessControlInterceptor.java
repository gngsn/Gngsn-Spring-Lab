package com.gngsn.accesswhitecidr.interceptor;

import com.gngsn.accesswhitecidr.service.AllowCidrCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.gngsn.accesswhitecidr.utils.IpUtil.getClientIp;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpAccessControlInterceptor implements HandlerInterceptor {

    private final AllowCidrCheckService allowCidrCheckService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestIp = getClientIp(request);

        if (allowCidrCheckService.isWhiteIp(requestIp)) {
            String requestURI = request.getRequestURI();

            log.warn("Forbidden access. request uri={}, client ip={}", requestURI, requestIp);

            response.sendError(403, "IP Forbidden");
            // redirect NOT AUTH PAGE or FORBIDDEN status
            return false;
        }

        return true;
    }
}
