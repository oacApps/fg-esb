package co.za.flash.ms.pep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PepMessagingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PepMessagingServiceApplication.class, args);
    }

}
