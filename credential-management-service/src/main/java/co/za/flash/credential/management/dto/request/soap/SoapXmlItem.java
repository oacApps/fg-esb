package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class SoapXmlItem {
    private SoapEnvFieldNames key;
    private String value;

    public SoapXmlItem(SoapEnvFieldNames key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toSoapXml() {
        if (!isValid()) return "";
        return key.getKeyFieldLeft() + this.value + key.getKeyFieldRight();
    }

    public boolean isValid() {
        return !StringUtil.isNullOrBlank(value);
    }
}
