package co.za.flash.credential.management.restclient;

import co.za.flash.credential.management.config.WSO2Config;
import co.za.flash.credential.management.dto.request.soap.*;
import co.za.flash.credential.management.dto.response.GetRoleListOfUserResponseDto;
import co.za.flash.credential.management.dto.response.GetUserListDto;
import co.za.flash.credential.management.dto.response.UserDto;
import co.za.flash.credential.management.dto.response.ErrorDto;
import co.za.flash.credential.management.dto.response.GeneralDtoResponse;
import co.za.flash.credential.management.helper.enums.ErrorCodes;
import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.interfaces.IApiResponseHandler;
import co.za.flash.credential.management.helper.interfaces.IUserService;
import co.za.flash.credential.management.helper.utils.RestTemplateUtil;
import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.model.config.IdentityServerUserManagerConfig;
import co.za.flash.credential.management.model.request.AddUserRequest;
import co.za.flash.credential.management.model.request.ChangePasswordRequest;
import co.za.flash.credential.management.model.request.ResetPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class UserSoapApiService extends BaseRestApiService implements IUserService {
    @Autowired
    private WSO2Config wso2Config;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public GeneralDtoResponse<UserDto> searchUser(String userNameWithDomain) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                ErrorDto dto = getErrorMessageFromRawResponse(rawResponse);
                if (dto == null)
                    dto = new ErrorDto(ErrorCodes.InvalidResponseFromSoapClient.description, rawResponse.getBody());
                return dto.getDetail();
            }
        };
        ResponseEntity<String> rawResponse = searchUsersApiCall(userNameWithDomain);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (handler.isDtoResponseSucceeded(rawResponse)) {
            GeneralDtoResponse<GetUserListDto> user = GetUserListDto.parseXml(dtoResponse.getData(), SoapEnvFieldNames.NsListUserResponse);
            if (user.isSuccessResponse() ) {
                if (user.getData().hasData()) {
                    String userName = user.getData().getUsers().get(0).getUserName();
                    String userId = "";
                    List<String> roles = List.of();
                    // now we found the data. check the role
                    rawResponse = getRoleListOfUserApiCall(userNameWithDomain);
                    if (rawResponse.getStatusCode() == HttpStatus.OK) {
                        GeneralDtoResponse<GetRoleListOfUserResponseDto> userRoles =
                                GetRoleListOfUserResponseDto.parseXml(rawResponse.getBody(), SoapEnvFieldNames.NsGetRoleListOfUserResponse);
                        if (userRoles.isSuccessResponse() && userRoles.getData().hasData() ) {
                            roles = userRoles.getData().getRoles();
                        }
                    }
                    return new GeneralDtoResponse<UserDto>(new UserDto(userId, userName, roles));
                }
                return new GeneralDtoResponse<UserDto>(ErrorCodes.NotFound.description);
            }
            return new GeneralDtoResponse<UserDto>(user.getError());
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> addUser(AddUserRequest request) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.ACCEPTED);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                ErrorDto dto = getErrorMessageFromRawResponse(rawResponse);
                if (dto == null)
                    dto = new ErrorDto(ErrorCodes.InvalidResponseFromSoapClient.description, rawResponse.getBody());
                return dto.getDetail();
            }
        };
        ResponseEntity<String> rawResponse = createUserApiCall(request);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (handler.isDtoResponseSucceeded(rawResponse)) {
            IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
            AddUserDto dto = new AddUserDto(request, config.getDomain(), config.getProfileRole());
            // get user roles
            List<String> roles = List.of();
            // now we found the data. check the role
            rawResponse = getRoleListOfUserApiCall(dto.getUserName().getValue());
            if (rawResponse.getStatusCode() == HttpStatus.OK) {
                GeneralDtoResponse<GetRoleListOfUserResponseDto> userRoles =
                        GetRoleListOfUserResponseDto.parseXml(rawResponse.getBody(), SoapEnvFieldNames.NsGetRoleListOfUserResponse);
                if (userRoles.isSuccessResponse() && userRoles.getData().hasData() ) {
                    roles = userRoles.getData().getRoles();
                }
            }
            return new GeneralDtoResponse<UserDto>(new UserDto("", dto.getUserName().getValue(), roles));
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> makeUserSelfManageable(String userNameWithDomain) {
        String roleName = wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole();
        return assignUserToGroup(userNameWithDomain, roleName);
    }

    @Override
    public GeneralDtoResponse<UserDto> assignUserToGroup(String userNameWithDomain, String groupName) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.ACCEPTED);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                ErrorDto dto = getErrorMessageFromRawResponse(rawResponse);
                if (dto == null)
                    dto = new ErrorDto(ErrorCodes.InvalidResponseFromSoapClient.description, rawResponse.getBody());
                return dto.getDetail();
            }
        };
        ResponseEntity<String> rawResponse = assignUserToGroupApiCall(userNameWithDomain, groupName);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (handler.isDtoResponseSucceeded(rawResponse)) {
            IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
            List<String> roles = List.of();
            // now we found the data. check the role
            rawResponse = getRoleListOfUserApiCall(userNameWithDomain);
            if (rawResponse.getStatusCode() == HttpStatus.OK) {
                GeneralDtoResponse<GetRoleListOfUserResponseDto> userRoles =
                        GetRoleListOfUserResponseDto.parseXml(rawResponse.getBody(), SoapEnvFieldNames.NsGetRoleListOfUserResponse);
                if (userRoles.isSuccessResponse() && userRoles.getData().hasData() ) {
                    roles = userRoles.getData().getRoles();
                }
            }
            return new GeneralDtoResponse<UserDto>(new UserDto("", userNameWithDomain, roles));
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> adminResetPassword(ResetPasswordRequest request) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.ACCEPTED);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                ErrorDto dto = getErrorMessageFromRawResponse(rawResponse);
                if (dto == null)
                    dto = new ErrorDto(ErrorCodes.InvalidResponseFromSoapClient.description, rawResponse.getBody());
                return dto.getDetail();
            }
        };
        ResponseEntity<String> rawResponse = adminResetPasswordApiCall(request);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (handler.isDtoResponseSucceeded(rawResponse)) {
            IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
            List<String> roles = List.of();
            // now we found the data. check the role
            rawResponse = getRoleListOfUserApiCall(request.getUserNameWithDomain());
            if (rawResponse.getStatusCode() == HttpStatus.OK) {
                GeneralDtoResponse<GetRoleListOfUserResponseDto> userRoles =
                        GetRoleListOfUserResponseDto.parseXml(rawResponse.getBody(), SoapEnvFieldNames.NsGetRoleListOfUserResponse);
                if (userRoles.isSuccessResponse() && userRoles.getData().hasData() ) {
                    roles = userRoles.getData().getRoles();
                }
            }
            return new GeneralDtoResponse<UserDto>(new UserDto("", request.getUserNameWithDomain(), roles));
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public boolean adminDeleteUser(String userNameWithDomain) {
        /*<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ser="http://service.ws.um.carbon.wso2.org">
   <soap:Header/>
   <soap:Body>
      <ser:deleteUser>
         <!--Optional:-->
         <ser:userName>nila@wso2support.com</ser:userName>
      </ser:deleteUser>
   </soap:Body>
</soap:Envelope>*/
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK ||
                        rawResponse.getStatusCode() == HttpStatus.ACCEPTED);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                ErrorDto dto = getErrorMessageFromRawResponse(rawResponse);
                if (dto == null)
                    dto = new ErrorDto(ErrorCodes.InvalidResponseFromSoapClient.description, rawResponse.getBody());
                return dto.getDetail();
            }
        };
        ResponseEntity<String> rawResponse = deleteUserApiCall(userNameWithDomain);
        return handler.isDtoResponseSucceeded(rawResponse);
    }

    @Override
    public GeneralDtoResponse<UserDto> changePassword(ChangePasswordRequest request) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.ACCEPTED);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                ErrorDto dto = getErrorMessageFromRawResponse(rawResponse);
                if (dto == null)
                    dto = new ErrorDto(ErrorCodes.InvalidResponseFromSoapClient.description, rawResponse.getBody());
                return dto.getDetail();
            }
        };
        ResponseEntity<String> rawResponse = changePasswordApiCall(request);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (handler.isDtoResponseSucceeded(rawResponse)) {
            IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();

            List<String> roles = List.of();
            // now we found the data. check the role
            rawResponse = getRoleListOfUserApiCall(request.getUserNameWithDomain());
            if (rawResponse.getStatusCode() == HttpStatus.OK) {
                GeneralDtoResponse<GetRoleListOfUserResponseDto> userRoles =
                        GetRoleListOfUserResponseDto.parseXml(rawResponse.getBody(), SoapEnvFieldNames.NsGetRoleListOfUserResponse);
                if (userRoles.isSuccessResponse() && userRoles.getData().hasData() ) {
                    roles = userRoles.getData().getRoles();
                }
            }
            return new GeneralDtoResponse<UserDto>(new UserDto("", request.getUserNameWithDomain(), roles));
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    private ResponseEntity<String> getUserListOfRoleApiCall(String groupName) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            GetUserListOfRoleDto postBody = new GetUserListOfRoleDto(groupName);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String> searchUsersApiCall(String userNameWithDomain) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            UserListDto postBody = new UserListDto(userNameWithDomain);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String> getRoleListOfUserApiCall(String userNameWithDomain) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            GetRoleListOfUserDto postBody = new GetRoleListOfUserDto(userNameWithDomain);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String> adminResetPasswordApiCall(ResetPasswordRequest request) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            ResetPasswordDto postBody = new ResetPasswordDto(request, wso2Config);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String> deleteUserApiCall(String userNameWithDomain) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            DeleteUserDto postBody = new DeleteUserDto(userNameWithDomain);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String>  assignUserToGroupApiCall(String userNameWithDomain, String groupName) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            UpdateRoleListOfUserDto postBody = new UpdateRoleListOfUserDto(userNameWithDomain, groupName);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String> changePasswordApiCall(ChangePasswordRequest request) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            ChangePasswordDto postBody = new ChangePasswordDto(request);
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }


    private String getUserApiUrl() {
        String configCategoryKey = "user";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim();
        return StringUtil.joinUrl(baseUrl, pathUrl, "");
    }

    private ResponseEntity<String> createUserApiCall(AddUserRequest request) {
        String url = getUserApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            AddUserDto postBody = new AddUserDto(request, config.getDomain(), config.getProfileRole());
            return restTemplateUtil.postForSoap(url, postBody, String.class);
        } catch (HttpClientErrorException hcee) {
            logError(hcee.getMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromHttpClientErrorException(hcee);
        } catch (RestClientException re) {
            logError(re.getLocalizedMessage());
            // There is a chance that the call hits the api and return a valid error.
            // get this error out
            return getValidErrorFromException(re.getLocalizedMessage());
        } catch (Exception e) {
            logError(e.getLocalizedMessage());
            return getValidErrorFromException(e.getLocalizedMessage());
        }
    }

    private ResponseEntity<String> getValidErrorFromHttpClientErrorException(HttpClientErrorException hcee) {
        String xmlBody = getXmlFromErrorDetails(hcee.getMessage());
        if (StringUtil.isNullOrBlank(xmlBody)) {
            xmlBody = hcee.getMessage();
        }
        return ResponseEntity.status(hcee.getStatusCode()).body(xmlBody);
    }

    private ResponseEntity<String> getValidErrorFromException(String message) {
        String xmlBody = getXmlFromErrorDetails(message);
        if (StringUtil.isNullOrBlank(xmlBody)) {
            xmlBody = message;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(xmlBody);
    }

    private String getXmlFromErrorDetails(String errorString) {
        String starter = "<?xml version='1.0' encoding='UTF-8'?>";
        String ender = "</soapenv:Envelope>";
        //<400 BAD_REQUEST Bad Request,500 Internal Server Error: [<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"><soapenv:Body><soapenv:Fault><faultcode>soapenv:Server</faultcode><faultstring>UserAlreadyExisting:Username already exists in the system. Please pick another username.</faultstring><detail/></soapenv:Fault></soapenv:Body></soapenv:Envelope>],[]>
        if (errorString.contains(starter) &&
            errorString.contains(ender)) {
            int indexStart = errorString.indexOf(starter);
            int indexEnd = errorString.lastIndexOf(ender) + ender.length();
            if (indexEnd >= errorString.length()) {
                return errorString.substring(indexStart);
            } else {
                return errorString.substring(indexStart, indexEnd);
            }
        }
        return "";
    }

    private ErrorDto getErrorMessageFromRawResponse(ResponseEntity<String> rawResponse) {
        return ErrorDto.parseXml(rawResponse.getBody());
    }

    @Override
    public void logError(String errorMessage) {
        logger.error(errorMessage);
    }
}