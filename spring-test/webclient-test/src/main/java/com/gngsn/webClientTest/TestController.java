package com.gngsn.webClientTest;

import com.gngsn.webClientTest.vo.ResVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController("/test")
//@RequestMapping("/test")
//@RequiredArgsConstructor
public class TestController {


	@PostMapping(value = "/200")
	public ResVO res200() {
		return new ResVO(true, HttpStatus.OK, "request success");
	}

	@PostMapping(value = "/400")
	public ResVO res400() {
        return new ResVO(false, HttpStatus.BAD_REQUEST, "invalid request data");
	}

	@PostMapping(value = "/401")
	public ResVO res401() {
		return new ResVO(false, HttpStatus.UNAUTHORIZED, "please check you authorization.");
	}

	@PostMapping(value = "/500")
	public ResVO res500() {
        return new ResVO(false, HttpStatus.INTERNAL_SERVER_ERROR, "system error. please inquery to system manager");
	}
}
