package com.gngsn.demo.deadlock;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class ReqDTO {

    @NotBlank(message = "'reqId' cannot be null.")
    private String reqId;

    @NotBlank(message = "'svcCd' cannot be null.")
    private String svcCd;

    @Valid
    @NotNull(message = "'detailList' cannot be null.")
    @Size(min = 1, max = 1_000, message = "The maximum number of 'purchaseList' is {max}.")
    private List<ReqDetailDTO> detailList;

    private LocalDateTime regYmdt;
    private LocalDateTime modYmdt;

}

/*
CREATE TABLE request (
  pk INT(20) NOT NULL AUTO_INCREMENT,
  req_id VARCHAR(20) NOT NULL,
  svc_cd VARCHAR(10) NOT NULL,
  user_id VARCHAR(100) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  reg_ymdt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  mod_ymdt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`pk`),
  UNIQUE KEY UNQ_userId_svcCd (`user_id`,`svc_cd`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8mb4;
 */
