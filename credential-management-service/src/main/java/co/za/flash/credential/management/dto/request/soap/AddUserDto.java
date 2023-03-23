package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.ISoapXmlDto;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.model.request.AddUserRequest;
import lombok.Data;

@Data
public class AddUserDto implements ISoapXmlDto {
    private static final SoapEnvFieldNames key = SoapEnvFieldNames.AddUser;
    private SoapXmlItem userName;
    private SoapXmlItem credential;
    private SoapXmlItem roleList;
    private SoapXmlItem requirePasswordChange;

    public AddUserDto(AddUserRequest request, String defaultDomain, String roleGroupName) {
        String domain = defaultDomain;
        if (!StringUtil.isNullOrBlank(request.getDomain())) {
            domain = request.getDomain();
        }
        String userName = domain + "/" + request.getUsername();
        String password = request.getPassword();
        this.userName = new SoapXmlItem(SoapEnvFieldNames.UserName, userName);
        this.credential = new SoapXmlItem(SoapEnvFieldNames.Credential, password);

        String role = "";
        if (request.isSelfManageable()) {
            role = roleGroupName;
        }
        this.roleList = new SoapXmlItem(SoapEnvFieldNames.RoleList, role);
        this.requirePasswordChange = new SoapXmlItem(SoapEnvFieldNames.RequirePasswordChange, "false");
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
        String credentialSoapXml = credential.toSoapXml();
        if (!StringUtil.isNullOrBlank(credentialSoapXml)) {
            sb.append(credentialSoapXml);
        }
        String roleListSoapXml = roleList.toSoapXml();
        if (!StringUtil.isNullOrBlank(roleListSoapXml)) {
            sb.append(roleListSoapXml);
        }
        String requirePasswordChangeSoapXml = requirePasswordChange.toSoapXml();
        if (!StringUtil.isNullOrBlank(requirePasswordChangeSoapXml)) {
            sb.append(requirePasswordChangeSoapXml);
        }

        sb.append(key.getKeyFieldRight());
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperBottom());
        return sb.toString();
    }
}
