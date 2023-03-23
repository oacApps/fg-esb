package co.za.flash.credential.management.config;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ServiceInfoConfig serviceInfoConfig;

    @Bean
    public Docket api() {

        Contact contact = new Contact("Muhammed Mashiur Rahman", "", "muhammed.rahman@flash.co.za");
        Collection<VendorExtension> vendorExtensions = new ArrayList<>();

        ApiInfo apiInfo = new ApiInfo("Credential Management Service",
                "API for Credential Management Service",
                "1.0.0", "",
                contact,
                "Flash",
                "",
                vendorExtensions);
        if (serviceInfoConfig != null && serviceInfoConfig.isLoaded()) {
            contact = new Contact(serviceInfoConfig.getContact().getName(), "", serviceInfoConfig.getContact().getEmail());
            apiInfo = new ApiInfo(serviceInfoConfig.getTitle(),
                    serviceInfoConfig.getDescription(),
                    "1.0.0", serviceInfoConfig.getTerms(),
                    contact,
                    serviceInfoConfig.getLicence().getName(),
                    serviceInfoConfig.getLicence().getUrl(),
                    vendorExtensions);
        }

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("co.za.flash.credential.management"))
                .paths(PathSelectors.any())
                .build();
    }
}
