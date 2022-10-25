package com.gngsn;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController("/api/v1")
public class TestController {

    @RequestMapping("/limit")
    public ResDTO failure(@RequestParam int id) {
        if (id > 20) {
            throw new RuntimeException();
        }
        return new ResDTO(200, "[Berlin Server] Request Success");
    }

    @RequestMapping("/limit/random")
    public ResDTO randomFailure() {
        Random random = new Random();
        int num = random.nextInt(100);
        if (num > 20) {
            throw new RuntimeException();
        }
        return new ResDTO(200, "[Berlin Server] Request Success");
    }


    @RequestMapping("/delay")
    public ResDTO delay(@RequestParam long delay) throws InterruptedException {
        System.out.println("delay: " + delay);
        Thread.sleep(delay);

        return new ResDTO(200, "[Berlin Server] Request Success");
    }
}
