package com.gngsn.springcore.dataBinding.converter;

import com.gngsn.springcore.dataBinding.Event;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class EventConverter {

    // 얘는 빈으로 등록해도 됨. -> ConverterRegister를 사용 -> 직접 설정할 일은 없음
//    @Component
    public static class StringToEventConverter implements Converter<String, Event> {

        @Override
        public Event convert(String source) {
            System.out.println("StringToEventConverter - convert");
            return new Event(Integer.parseInt(source));
        }
    }

    public static class EventToStringConverter implements Converter<Event, String> {

        @Override
        public String convert(Event source) {
            System.out.println("EventToStringConverter - convert");
            return source.getId().toString();
        }
    }

}
