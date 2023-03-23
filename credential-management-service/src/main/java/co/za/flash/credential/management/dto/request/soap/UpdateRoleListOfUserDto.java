package co.za.flash.credential.management.dto.request.soap;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.ISoapXmlDto;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateRoleListOfUserDto implements ISoapXmlDto {
    private static final SoapEnvFieldNames key = SoapEnvFieldNames.UpdateRoleListOfUser;
    private SoapXmlItem userName;
    private ArrayList<SoapXmlItem> newRoles;
    private ArrayList<SoapXmlItem> deleteRoles;

    public UpdateRoleListOfUserDto(String userNameWithDomain, List<String> newRoleNames, List<String> deleteRoleNames) {
        this.userName = new SoapXmlItem(SoapEnvFieldNames.UserName, userNameWithDomain);
        newRoles = new ArrayList<>();
        if (newRoleNames != null && !newRoleNames.isEmpty()) {
            newRoleNames.stream().forEach(name-> {
                if (!StringUtil.isNullOrBlank(name))
                    newRoles.add(new SoapXmlItem(SoapEnvFieldNames.NewRoles, name));
            });
        }
        deleteRoles = new ArrayList<>();
        if (deleteRoleNames != null && !deleteRoleNames.isEmpty()) {
            deleteRoleNames.stream().forEach(name-> {
                if (!StringUtil.isNullOrBlank(name))
                    deleteRoles.add(new SoapXmlItem(SoapEnvFieldNames.DeletedRoles, name));
            });
        }
    }
    public UpdateRoleListOfUserDto(String userNameWithDomain, String newRoleName) {
        this.userName = new SoapXmlItem(SoapEnvFieldNames.UserName, userNameWithDomain);
        newRoles = new ArrayList<>();
        if (!StringUtil.isNullOrBlank(newRoleName)) {
            newRoles.add(new SoapXmlItem(SoapEnvFieldNames.NewRoles, newRoleName));
        }
        deleteRoles = new ArrayList<>();
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
        newRoles.stream().forEach(roleItem -> {
            String soapXml = roleItem.toSoapXml();
            if (!StringUtil.isNullOrBlank(soapXml)) {
                sb.append(soapXml);
            }
        });
        deleteRoles.stream().forEach(roleItem -> {
            String soapXml = roleItem.toSoapXml();
            if (!StringUtil.isNullOrBlank(soapXml)) {
                sb.append(soapXml);
            }
        });

        sb.append(key.getKeyFieldRight());
        sb.append(SoapEnvelopeParser.appendEnvelopeBodyWrapperBottom());
        return sb.toString();
    }
}
