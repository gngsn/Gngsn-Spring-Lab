package com.gngsn.demo.deadlock;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ReqDetailDTO {

    @NotBlank(message = "'userId' cannot be null.")
    private String userId;

    @NotBlank(message = "'userName' cannot be null.")
    private String userName;

}

