package com.gngsn.demo.referer;

import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;


@RestController
public class httpRefererController {

    @RequestMapping("/test")
    public ResponseEntity<String> itJustTest(HttpServletRequest request) {
        this.checkReferer("/");

        return ResponseEntity.ok().body("request success.");
    }

    private boolean checkReferer(String domain) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String referer = request.getHeader("referer");

        if (referer.isEmpty()) {
            return false;
        }

        try {
            referer = new URI(referer).getPath();    // domain, parameter 제외하고 uri만 get
            return referer.startsWith(domain);
        } catch (Exception e) {
            return false;
        }
    }
}
