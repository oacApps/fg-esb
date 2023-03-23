package co.za.flash.credential.management.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.model.config.IdentityServerConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("wso2-identity-server")
@Data
public class WSO2Config implements IConfigLoaded {
    private IdentityServerConfig production;
    private IdentityServerConfig sandbox;
    private String isUsingSandbox;
    private String isUsingRestApi;

    @Override
    public boolean isLoaded() {
        return (usingSandbox() && sandbox.isLoaded()) || (!usingSandbox() && production.isLoaded());
    }

    public boolean usingSandbox() {
        try {
            return Boolean.parseBoolean(isUsingSandbox);
        } catch (Exception e) {
            //
        }
        return true;
    }

    public boolean usingRestApi() {
        try {
            return Boolean.parseBoolean(isUsingRestApi);
        } catch (Exception e) {
            //
        }
        return true;
    }

    public IdentityServerConfig getCurrentInstanceConfig() {
        if (this.isLoaded() && usingSandbox()) {
            return sandbox;
        } else if (this.isLoaded() && !usingSandbox()) {
            return production;
        }
        return null;
    }
}
