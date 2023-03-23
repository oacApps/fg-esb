package co.za.flash.esb.ricaregistration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api() {

        Contact contact = new Contact("Muhammed Mashiur Rahman", "", "muhammed.rahman@flash.co.za");
        Collection<VendorExtension> vendorExtensions = new ArrayList<>();

        ApiInfo apiInfo = new ApiInfo("RICA API",
                "ESB API for RICA API",
                "1.0.0", "",
                contact,
                "Flash",
                "",
                vendorExtensions);

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("co.za.flash.esb.ricaregistration"))
                .paths(PathSelectors.any())
                .build();
    }
}
