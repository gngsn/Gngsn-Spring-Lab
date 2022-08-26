package com.gngsn.webClientTest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReqDTO {

	@NotEmpty
	private String type;

	@NotNull
	private LocalDateTime requestTime;
}


