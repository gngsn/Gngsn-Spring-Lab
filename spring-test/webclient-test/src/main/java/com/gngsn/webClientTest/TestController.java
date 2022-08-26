package com.gngsn.webClientTest;

import com.gngsn.webClientTest.vo.ReqDTO;
import com.gngsn.webClientTest.vo.ResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


	@RequestMapping(value = "/200")
	public ResponseEntity<ResDTO> res200() {
		log.info("Request res 200");
		return new ResponseEntity<>(new ResDTO(false, HttpStatus.OK, "request success"), HttpStatus.OK);
	}

	@RequestMapping(value = "/400")
	public ResponseEntity<ResDTO> res400(ReqDTO reqDTO) {
		log.info("Request res400 | req body: {}", reqDTO);
        return new ResponseEntity<>(new ResDTO(false, HttpStatus.BAD_REQUEST, "invalid request data"), HttpStatus.BAD_REQUEST);
	}


	@RequestMapping(value = "/500")
	public ResponseEntity<ResDTO> res500() {
		log.info("Request res500");
		return new ResponseEntity<>(new ResDTO(false, HttpStatus.INTERNAL_SERVER_ERROR, "system error."), HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@RequestMapping(value = "/timeout")
	public ResponseEntity<ResDTO> timeout(ReqDTO reqDTO) throws InterruptedException {
		log.info("timeout test");
		Thread.sleep(10_000);
		return new ResponseEntity<>(new ResDTO(false, HttpStatus.OK, "request success"), HttpStatus.OK);
	}
}
