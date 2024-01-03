package com.goldpiecessoftware.httptester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class HttpTesterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpTesterApplication.class, args);
    }

}
