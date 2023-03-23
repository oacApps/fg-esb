package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.ISoapXmlDto;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;

public class UserListDto implements ISoapXmlDto {
    private static final SoapEnvFieldNames key = SoapEnvFieldNames.ListUsers;
    private SoapXmlItem filter;
    private SoapXmlItem maxItemLimit;

    public UserListDto(String userNameWithDomain) {
        this.filter = new SoapXmlItem(SoapEnvFieldNames.Filter, userNameWithDomain);
        this.maxItemLimit = new SoapXmlItem(SoapEnvFieldNames.MaxItemLimit, "10");
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

        String userNameSoapXml = filter.toSoapXml();
        if (!StringUtil.isNullOrBlank(userNameSoapXml)) {
            sb.append(userNameSoapXml);
        }
        String credentialSoapXml = maxItemLimit.toSoapXml();
        if (!StringUtil.isNullOrBlank(credentialSoapXml)) {
            sb.append(credentialSoapXml);
        }

        sb.append(key.getKeyFieldRight());
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperBottom());
        return sb.toString();
    }
}
