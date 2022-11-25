package com.gngsn.springcore.nullSafety;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class AppRunner10 implements ApplicationRunner {

    @Autowired
    EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String message = eventService.createEvent(null);
        // Preference > Compiler > Configuration annotation... 에 lombok.NonNull 추가해야 warning 뜸
    }
}
