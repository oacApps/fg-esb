package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.ISoapXmlDto;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.model.request.ChangePasswordRequest;

public class ChangePasswordDto implements ISoapXmlDto {
    private static final SoapEnvFieldNames key = SoapEnvFieldNames.UpdateUserCredential;
    private SoapXmlItem userName;
    private SoapXmlItem currentCredential;
    private SoapXmlItem newCredential;

    public ChangePasswordDto(ChangePasswordRequest request) {
        this.userName = new SoapXmlItem(SoapEnvFieldNames.UserName, request.getUserNameWithDomain());
        this.currentCredential = new SoapXmlItem(SoapEnvFieldNames.OldCredential, request.getCurrentPassword());
        this.newCredential = new SoapXmlItem(SoapEnvFieldNames.NewCredential, request.getNewPassword());
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

        String userNameSoapXml = userName.toSoapXml();
        if (!StringUtil.isNullOrBlank(userNameSoapXml)) {
            sb.append(userNameSoapXml);
        }
        // Notice: new password MUST in front of the old one.
        // otherwise: 35001 - Un-expected error during pre update credential, Password pattern policy violated.
        String credentialSoapXml1 = newCredential.toSoapXml();
        if (!StringUtil.isNullOrBlank(credentialSoapXml1)) {
            sb.append(credentialSoapXml1);
        }
        String credentialSoapXml = currentCredential.toSoapXml();
        if (!StringUtil.isNullOrBlank(credentialSoapXml)) {
            sb.append(credentialSoapXml);
        }

        sb.append(key.getKeyFieldRight());
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperBottom());
        return sb.toString();
    }
}
