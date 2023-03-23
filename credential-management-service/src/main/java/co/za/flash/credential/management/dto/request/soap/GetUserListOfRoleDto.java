package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.ISoapXmlDto;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class GetUserListOfRoleDto  implements ISoapXmlDto {
    private static final SoapEnvFieldNames key = SoapEnvFieldNames.GetUserListOfRole;
    private SoapXmlItem roleName;

    public GetUserListOfRoleDto(String roleName) {
        this.roleName = new SoapXmlItem(SoapEnvFieldNames.RoleName, roleName);
    }
    @Override
    public String getSoapActionHeader() {
        return key.soapActionHeader;
    }

    @Override
    public String getSoapEnvelope() {
        StringBuilder sb = new StringBuilder();
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperTop());
        sb.append(key.getKeyFieldLeft());

        String userNameSoapXml = roleName.toSoapXml();
        if (!StringUtil.isNullOrBlank(userNameSoapXml)) {
            sb.append(userNameSoapXml);
        }

        sb.append(key.getKeyFieldRight());
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperBottom());
        return sb.toString();
    }
}
