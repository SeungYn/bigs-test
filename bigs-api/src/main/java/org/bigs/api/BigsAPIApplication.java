package org.bigs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.bigs"})
@SpringBootApplication()
public class BigsAPIApplication {
    public static void main(String[] args){
        SpringApplication.run(BigsAPIApplication.class);
    }
}
