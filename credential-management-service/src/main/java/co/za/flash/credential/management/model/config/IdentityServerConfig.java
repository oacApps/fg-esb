package co.za.flash.credential.management.model.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import lombok.Data;

@Data
public class IdentityServerConfig implements IConfigLoaded {
    private IdentityServerUrlConfig url;
    private Wso2SoapConfig soap;
    private IdentityServerUserManagerConfig userManagement;

    @Override
    public boolean isLoaded() {
        return (url != null && url.isLoaded()) && (userManagement != null && userManagement.isLoaded());
    }
}
