package com.sds.prometheus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@SuppressWarnings("unused")
public class PrometheusApplication {

    @GetMapping
    public String ok() {
        return "ok";
    }

    @GetMapping("error")
    public ResponseEntity<String> error() {
        return new ResponseEntity<>("Hello World!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void main(String[] args) {
        SpringApplication.run(PrometheusApplication.class, args);
    }

}
