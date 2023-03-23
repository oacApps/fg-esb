package co.za.flash.credential.management.model.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class IdentityServerUserManagerConfig implements IConfigLoaded {
    private String domain;
    private String profileRole;
    private String profileRoleDefaultId; // for the case that could not get the profile group via a search
    private String forgetPasswordBaseUrl;

    @Override
    public boolean isLoaded() {
        return !StringUtil.isNullOrBlank(domain) &&
                !StringUtil.isNullOrBlank(profileRole) && !StringUtil.isNullOrBlank(forgetPasswordBaseUrl);
    }
}
