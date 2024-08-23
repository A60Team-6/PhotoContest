package com.telerikacademy.web.photocontest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories(basePackages = "com.telerikacademy.web.photocontest.config.user")
//@EntityScan(basePackages = "com.telerikacademy.web.photocontest.config.user")
@SpringBootApplication
public class PhotoContestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoContestApplication.class, args);

    }

}
