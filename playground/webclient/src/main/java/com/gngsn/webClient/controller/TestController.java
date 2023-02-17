package com.gngsn.webClient.controller;

import com.gngsn.webClient.exception.BadWebClientRequestException;
import com.gngsn.webClient.vo.ResResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class TestController {

	private final Logger log = LoggerFactory.getLogger(TestController.class);


	@RequestMapping(value = "/200")
	public ResponseEntity<ResResult> res200() {
		log.info("Request res 200");
		return new ResponseEntity<>(ResResult.success("Request Success"), HttpStatus.OK);
	}

	@RequestMapping(value = "/400")
	public ResponseEntity<ResResult> res400() {
		log.info("Request res400");
		return new ResponseEntity<>(ResResult.success("Bad Request"), HttpStatus.BAD_REQUEST);
	}


	@RequestMapping(value = "/500")
	public ResponseEntity<ResResult> res500() {
		log.info("Request res500");
		throw new BadWebClientRequestException(500, "Server Error");
	}


	@RequestMapping(value = "/timeout")
	public ResResult timeout() throws InterruptedException {
		log.info("timeout test");
		Thread.sleep(10_000);
		return ResResult.success("timeout test");
	}
}
