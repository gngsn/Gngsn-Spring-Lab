package com.gngsn.accesswhitecidr.service.impl;

import com.gngsn.accesswhitecidr.service.AllowCidrCheckService;
import com.gngsn.accesswhitecidr.service.dao.WhiteCidrDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import static com.gngsn.accesswhitecidr.utils.IpUtil.getClientIp;

@Service
@RequiredArgsConstructor
public class AllowCidrCheckServiceImpl implements AllowCidrCheckService {

    private final WhiteCidrDAO whiteCidrDAO;

    /**
     * client IP를 받아서
     * 등록된 white CIDR 리스트에 포함되는지 체크
     *
     * @param clientIp
     * @return boolean  - white IP (True), NOT white IP (False)
     */
    public boolean isWhiteIp(String clientIp) {

        return whiteCidrDAO.selectWhiteCidrList()
            .stream()
            .anyMatch(cidr ->
                (new IpAddressMatcher(cidr)).matches(clientIp)
            );
    }
}
