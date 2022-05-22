package com.gngsn.demo;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateTimeTest {

    @Test
    void dateTest() {
        Date date = new Date();
        long time = date.getTime();

        System.out.println(date);
        System.out.println(time);


        Calendar birthDay = new GregorianCalendar(2022, GregorianCalendar.AUGUST, 12);
        System.out.println(birthDay.getTime());
    }


    @Test
    void instanceTest() {
        Instant instant = Instant.now();        // 기준시 UTC, GMT
        System.out.println(instant);

        ZoneId zone = ZoneId.systemDefault();   // Asia/Seoul
        System.out.println(zone);

        ZoneId zone2 = ZoneId.of("Asia/Seoul");


        ZonedDateTime zonedDateTime = instant.atZone(zone);
        System.out.println(zonedDateTime);
    }


    @Test
    void durationTest() {
        Instant instantNow = Instant.now();
        Instant plus = instantNow.plus(10, ChronoUnit.SECONDS);
        Duration between = Duration.between(instantNow, plus);

        System.out.println(between.getSeconds());
    }

    @Test
    void localDateTimeToDate() {
//        LocalDateTime localDateTime = LocalDateTime.parse("2022-05-20T15:15:30");
        LocalDateTime localDateTime = LocalDateTime.now();

        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

// 1.
        Date date = Date.from(instant);
        System.out.println(date);
// 2.
        Instant instant2 = localDateTime.toInstant(ZoneOffset.of("+09:00"));
        Date date2 = Date.from(instant2);
        System.out.println(date2);

// 3.
        Instant instant3 = Timestamp.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).toInstant();
        Date date3 = Date.from(instant3);
        System.out.println(date3.getTime());

        Timestamp timestamp = Timestamp.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        date = new Date(timestamp.getTime());
        System.out.println(date.getTime());
    }

    void periodTest() {
        LocalDate birth = LocalDate.of(1998, Month.AUGUST, 12);
        LocalDate today = LocalDate.now();

        Period between = Period.between(birth, today);
        // or
        Period until = today.until(birth);

        System.out.println(between.get(ChronoUnit.DAYS));
        System.out.println(between.getYears());
        System.out.println(between.getMonths());
        System.out.println(between.getDays());

        // Total Days 구하고 싶다면!
        long date = ChronoUnit.DAYS.between(birth, today);
        System.out.println(date);
    }

}
