package co.za.flash.credential.management.helper.enums;

import com.sun.istack.NotNull;

public enum SoapEnvFieldNames {
    Envelop("soapenv:Envelope", ""),
    Header("soapenv:Header", ""),
    Body("soapenv:Body", ""),
    AddUser("ser:addUser", "urn:addUser"),
    DeleteUser("ser:deleteUser", "urn:deleteUser"),
    UpdateRoleListOfUser("ser:updateRoleListOfUser", "urn:updateRoleListOfUser"),
    GetRoleListOfUser("ser:getRoleListOfUser", "urn:getRoleListOfUser"),
    GetUserListOfRole("ser:getUserListOfRole", "urn:getUserListOfRole"),
    UpdateUserCredential("ser:updateCredential", "urn:updateCredential"),
    UpdateCredentialAdmin("ser:updateCredentialByAdmin", ""),
    NewCredential("ser:newCredential", ""),
    OldCredential("ser:oldCredential", ""),
    UserName("ser:userName", ""),
    Credential("ser:credential", ""),
    RoleList("ser:roleList", ""),
    RequirePasswordChange("ser:requirePasswordChange", ""),
    ListUsers("ser:listUsers", "urn:listUsers"),
    Filter("ser:filter", ""),
    MaxItemLimit("ser:maxItemLimit", ""),
    NewRoles("ser:newRoles", ""),
    DeletedRoles("ser:deletedRoles", ""),
    RoleName("ser:roleName", ""),
    NsListUserResponse("ns:listUsersResponse", ""),
    NsGetUserListOfRoleResponse("ns:getUserListOfRoleResponse", ""),
    NsGetRoleListOfUserResponse("ns:getRoleListOfUserResponse", ""),
    NsReturn("ns:return", ""),
    Error("soapenv:Fault", ""),
    ErrorCode("faultcode", ""),
    ErrorString("faultstring", "");

    public final String fieldName;
    public final String soapActionHeader;

    private SoapEnvFieldNames(@NotNull String fieldName, @NotNull String soapActionHeader) {
        this.fieldName = fieldName;
        this.soapActionHeader = soapActionHeader;
    }

    public String getKeyFieldLeft() {
        if (this == Envelop) {
            return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.ws.um.carbon.wso2.org\">";
        }
        return "<" + fieldName + ">";
    }

    public String getKeyFieldRight() {
        return "</"+ fieldName + ">";
    }

    public String getEmptyKeyField() { return "<" + fieldName + "/>"; }
}
