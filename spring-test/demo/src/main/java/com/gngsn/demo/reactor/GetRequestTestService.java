package com.gngsn.demo.reactor;

import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.URIParameter;

@Service
public class GetRequestTestService {
    @Autowired
    WebClient webClient;

    public Flux<Employee> findAll() {
        Flux<Employee> employeeFlux = webClient.get()
                .uri("/employees")
                .retrieve()
                .bodyToFlux(Employee.class);
        employeeFlux.subscribe(System.out::println);

        return employeeFlux;
    }

    public String uriBuilder(String uri, MultiValueMap<String, String> params) {
        return UriComponentsBuilder
                .fromUriString(uri)
                .queryParams(params)
                .toUriString();
    }

    public Mono<Employee> create(Employee empl) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("id", "1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map);

        return webClient.post()
                .uri("/employees")
                .body(request, Employee.class)
                .retrieve()
                .bodyToMono(Employee.class);
    }

    public Mono<Employee> createById(Integer id) {
        return webClient.post()
                        .uri("/employees/" + id)
                        .retrieve()

                        /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                                    clientResponse -> Mono.empty())*/
                        .bodyToMono(Employee.class);

    }

    public Mono<Employee> findById(Integer id) {
        return webClient.get()
                .uri("/employees/" + id)
                .retrieve()
                /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                            clientResponse -> Mono.empty())*/
                .bodyToMono(Employee.class);
        }
}

class Employee {

}