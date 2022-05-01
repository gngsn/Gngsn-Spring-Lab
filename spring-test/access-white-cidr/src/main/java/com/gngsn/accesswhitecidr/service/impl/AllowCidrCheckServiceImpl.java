package com.gngsn.accesswhitecidr.service.impl;

import com.gngsn.accesswhitecidr.service.AllowCidrCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllowCidrCheckServiceImpl implements AllowCidrCheckService {



    public boolean isWhiteIp() {
        return false;
    }

    public boolean isWhiteIp(String clientIp) {
        return false;
    }

}
