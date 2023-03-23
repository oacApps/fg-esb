package co.za.flash.credential.management.model.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class Wso2SoapConfig implements IConfigLoaded {
    private String contextPath;
    private String defaultUri;

    @Override
    public boolean isLoaded() {
        return !StringUtil.isNullOrBlank(contextPath) && !StringUtil.isNullOrBlank(defaultUri);
    }
}
