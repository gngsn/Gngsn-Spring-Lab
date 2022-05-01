package com.gngsn.accesswhitecidr.service;

public interface AllowCidrCheckService {

    boolean isWhiteIp();

    boolean isWhiteIp(String clientIp);

}
