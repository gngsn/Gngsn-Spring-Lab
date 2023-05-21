package com.gngsn;

import com.gngsn.entity.User;

import com.gngsn.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;

@SpringBootApplication
public class JpaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }

    @Bean
    public ApplicationRunner configure(UserRepository userRepository) {
        return env -> {
            User user1 = new User("gngsn", LocalDate.of(2023, Month.AUGUST, 12));
            User user2 = new User("mike", LocalDate.of(2020, Month.JANUARY, 18));

            userRepository.save(user1);
            userRepository.save(user2);

            userRepository.findAll().forEach(System.out::println);
        };
    }

}
