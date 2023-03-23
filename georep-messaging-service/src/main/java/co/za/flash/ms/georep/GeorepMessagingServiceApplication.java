package co.za.flash.ms.georep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GeorepMessagingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeorepMessagingServiceApplication.class, args);
    }

}
