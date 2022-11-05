package com.gngsn.webClient;

import com.gngsn.webClient.vo.ReqDTO;
import com.gngsn.webClient.vo.ResResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	private final Logger log = LoggerFactory.getLogger(TestController.class);


	@RequestMapping(value = "/200")
	public ResResult res200() {
		log.info("Request res 200");
		return ResResult.success();
	}

	@RequestMapping(value = "/400")
	public ResResult res400(ReqDTO reqDTO) {
		log.info("Request res400 | req body: {}", reqDTO);
        return ResResult.error(400, "400 error");
	}


	@RequestMapping(value = "/500")
	public ResResult res500() {
		log.info("Request res500");
		return ResResult.error("Interval Server Error");
	}


	@RequestMapping(value = "/timeout")
	public ResResult timeout(ReqDTO reqDTO) throws InterruptedException {
		log.info("timeout test");
		Thread.sleep(10_000);
		return ResResult.success();
	}
}
