package com.gngsn.springcore.dataBinding.formatter;

import com.gngsn.springcore.dataBinding.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

//@Component
public class EventFormatter implements Formatter<Event> {

//    @Autowired
//    MessageSource messageSource;

    @Override
    public Event parse(String text, Locale locale) throws ParseException {
        return new Event(Integer.parseInt(text));
    }

    @Override
    public String print(Event object, Locale locale) {
//        local 해당하는 메세지를 받아올 수도 있다 ~
//        messageSource.getMessage("title", locale);
        return object.getId().toString();
    }
}
