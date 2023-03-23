package co.za.flash.credential.management.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.model.config.ContactInfo;
import co.za.flash.credential.management.model.config.LicenceInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("certificate-manager")
@Data
public class ServiceInfoConfig implements IConfigLoaded {
    private String title;
    private String description;
    private ContactInfo contact;
    private String terms;
    private int threadpool;
    private LicenceInfo licence;

    @Override
    public boolean isLoaded() {
        return (title != null && !title.isBlank()) && contact.isLoaded() && licence.isLoaded();
    }
}
