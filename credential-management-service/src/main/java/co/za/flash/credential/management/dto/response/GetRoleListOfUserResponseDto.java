package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import co.za.flash.credential.management.helper.utils.StringUtil;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class GetRoleListOfUserResponseDto {
    private List<String> roles;

    private GetRoleListOfUserResponseDto() {
        roles = List.of();
    }

    private GetRoleListOfUserResponseDto(List<String> result) {
        this.roles = result;
    }

    public boolean hasData() {
        return !this.roles.isEmpty();
    }

    public List<String> getRoles() {
        return roles;
    }

    public static GeneralDtoResponse<GetRoleListOfUserResponseDto> parseXml(String rawResponse, SoapEnvFieldNames rootField) {
        /*<?xml version='1.0' encoding='UTF-8'?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
    <soapenv:Body>
        <ns:getRoleListOfUserResponse xmlns:ns="http://service.ws.um.carbon.wso2.org" xmlns:ax2730="http://dao.service.ws.um.carbon.wso2.org/xsd" xmlns:ax2728="http://common.mgt.user.carbon.wso2.org/xsd" xmlns:ax2732="http://tenant.core.user.carbon.wso2.org/xsd" xmlns:ax2724="http://core.user.carbon.wso2.org/xsd" xmlns:ax2725="http://api.user.carbon.wso2.org/xsd">
            <ns:return>Internal/everyone</ns:return>
            <ns:return>Internal/scim-app-user</ns:return>
        </ns:getRoleListOfUserResponse>
    </soapenv:Body>
</soapenv:Envelope>*/
        Node parentNode = SoapEnvelopeParser.getNodeFromBody(rawResponse, rootField.fieldName);
        if (parentNode != null) {
            ArrayList<String> roleList = new ArrayList<>();
            List<Node> list = SoapEnvelopeParser.getChildNodes(parentNode, SoapEnvFieldNames.NsReturn.fieldName);
            if (!list.isEmpty()) {
                list.stream().forEach(item -> {
                    String role = item.getTextContent();
                    if (!StringUtil.isNullOrBlank(role)) {
                        roleList.add(role);
                    }
                });
            }
            if (roleList.isEmpty()){
                return new GeneralDtoResponse(new GetRoleListOfUserResponseDto());
            }
            return new GeneralDtoResponse(new GetRoleListOfUserResponseDto(roleList));
        }
        return new GeneralDtoResponse("Parsing error");
    }
}
