package co.za.flash.esb.electricity.config;

import co.za.flash.esb.electricity.restservice.ConsumeWebService;
import co.za.flash.esb.electricity.service.ElectricityService;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.pojo.DBLoggingPayload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ElectricityService electricityService() { return new ElectricityService(); }

    @Bean
    public ConsumeWebService consumeWebService() { return new ConsumeWebService(); }

    @Bean
    public JwtExtractor jwtExtractor() { return new JwtExtractor(); }

    @Bean
    public DBLoggingPayload dbLoggingPayload() { return new DBLoggingPayload(); }

}
