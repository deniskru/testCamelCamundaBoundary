package com.tck.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableProcessApplication
public class CamundaCamelApplication {
        public static void main(String... args) {
        SpringApplication.run(CamundaCamelApplication.class, args);
    }
}
