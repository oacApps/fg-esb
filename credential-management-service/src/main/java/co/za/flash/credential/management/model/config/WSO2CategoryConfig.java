package co.za.flash.credential.management.model.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class WSO2CategoryConfig implements IConfigLoaded {
    private String scim;
    private String scim2;

    @Override
    public boolean isLoaded() {
        return !StringUtil.isNullOrBlank(scim) && !StringUtil.isNullOrBlank(scim2);
    }
}
