package co.za.flash.credential.management.model.config;

import co.za.flash.credential.management.helper.interfaces.IConfigLoaded;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

import java.util.Map;

@Data
public class IdentityServerUrlConfig implements IConfigLoaded {
    private String base;
    private Map<String, WSO2CategoryConfig> categories;

    @Override
    public boolean isLoaded() {
        boolean flag = !StringUtil.isNullOrBlank(base) && (categories != null && !categories.isEmpty());
        if (flag) {
            for (String key : categories.keySet()) {
                boolean flag1 = categories.get(key).isLoaded();
                if (!flag1) return false;
            }
            return true;
        }
        return false;
    }
}
