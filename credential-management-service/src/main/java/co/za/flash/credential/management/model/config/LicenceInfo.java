package co.za.flash.credential.management.model.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class LicenceInfo implements IConfigLoaded {
    private String name;
    private String url;

    @Override
    public boolean isLoaded() {
        return (!StringUtil.isNullOrBlank(name)) && (!StringUtil.isNullOrBlank(url));
    }
}
