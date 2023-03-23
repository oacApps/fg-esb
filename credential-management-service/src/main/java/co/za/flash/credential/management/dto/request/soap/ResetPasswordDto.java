package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.config.WSO2Config;
import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.ISoapXmlDto;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.helper.utils.TokenCredentialHelper;
import co.za.flash.credential.management.model.request.ResetPasswordRequest;

public class ResetPasswordDto implements ISoapXmlDto {
    private static final SoapEnvFieldNames key = SoapEnvFieldNames.UpdateCredentialAdmin;
    private SoapXmlItem userName;
    private SoapXmlItem newCredential;
    private String tokenHeader;

    public ResetPasswordDto(ResetPasswordRequest request, WSO2Config config) {
        this.userName = new SoapXmlItem(SoapEnvFieldNames.UserName, request.getUserNameWithDomain());
        this.newCredential = new SoapXmlItem(SoapEnvFieldNames.NewCredential, request.getNewPassword());
        tokenHeader = new TokenCredentialHelper().getCurrentCredential(config);
    }

    @Override
    public String getSoapActionHeader() {
        return "Basic " + tokenHeader;
    }

    @Override
    public String getSoapEnvelope() {
        StringBuilder sb = new StringBuilder();
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperTop());
        sb.append(key.getKeyFieldLeft());

        String userNameSoapXml = userName.toSoapXml();
        if (!StringUtil.isNullOrBlank(userNameSoapXml)) {
            sb.append(userNameSoapXml);
        }
        String credentialSoapXml1 = newCredential.toSoapXml();
        if (!StringUtil.isNullOrBlank(credentialSoapXml1)) {
            sb.append(credentialSoapXml1);
        }

        sb.append(key.getKeyFieldRight());
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperBottom());
        return sb.toString();
    }
}
