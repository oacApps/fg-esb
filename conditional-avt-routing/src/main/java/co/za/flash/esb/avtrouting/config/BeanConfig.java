package co.za.flash.esb.avtrouting.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    private final int HTTP_CONNECT_TIMEOUT =35000;
    private final int HTTP_READ_TIMEOUT = 30000;

    /*@Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
        clientHttpRequestFactory.setReadTimeout(HTTP_READ_TIMEOUT);
        return clientHttpRequestFactory;
    }*/
}
