package co.za.flash.credential.management.restclient;

import co.za.flash.credential.management.config.WSO2Config;
import co.za.flash.credential.management.dto.request.rest.UpdateGroupPatchDto;
import co.za.flash.credential.management.dto.request.rest.UpdateUserPatchDto;
import co.za.flash.credential.management.dto.response.*;
import co.za.flash.credential.management.helper.enums.ErrorCodes;
import co.za.flash.credential.management.helper.interfaces.IApiResponseHandler;
import co.za.flash.credential.management.helper.interfaces.IUserService;
import co.za.flash.credential.management.helper.utils.RestTemplateUtil;
import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.model.config.IdentityServerUserManagerConfig;
import co.za.flash.credential.management.model.request.AddUserRequest;
import co.za.flash.credential.management.model.request.ChangePasswordRequest;
import co.za.flash.credential.management.model.request.ResetPasswordRequest;
import co.za.flash.credential.management.model.request.UpdateEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRestApiService extends BaseRestApiService implements IUserService {
    @Autowired
    private WSO2Config wso2Config;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // region IUserService calls

    @Override
    public GeneralDtoResponse<UserDto> searchUser(String userNameWithDomain) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        ResponseEntity<String> rawResponse = searchUserApiCall(userNameWithDomain);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (dtoResponse.isSuccessResponse()) {
            GeneralDtoResponse<GetUserListDto> usersList = GetUserListDto.parseResponse(rawResponse.getBody());
            if (usersList.isSuccessResponse() && usersList.getData().getUsers().size() > 0) {
                return new GeneralDtoResponse<UserDto>(usersList.getData().getUsers().get(0));
            }
            return new GeneralDtoResponse<UserDto>(usersList.getError());
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> adminResetPassword(ResetPasswordRequest request) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        ResponseEntity<String> rawResponse = searchUserApiCall(request.getUserNameWithDomain());
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (dtoResponse.isSuccessResponse()) {
            GeneralDtoResponse<GetUserListDto> users = GetUserListDto.parseResponse(rawResponse.getBody());
            if (users.isSuccessResponse() && users.getData().hasData()) {
                ResponseEntity<String> rawResponseReset = resetPasswordApiCall(users.getData().getUsers().get(0).getId(), request.getNewPassword());
                GeneralDtoResponse<String> dtoResponseReset = handleResponse(handler, rawResponseReset);
                if (dtoResponseReset.isSuccessResponse()) {
                    return UserDto.parseResponse(rawResponseReset.getBody());
                }
            } else
                return new GeneralDtoResponse<UserDto>(ErrorCodes.EmptyResponseFromRestClient.description);
        } else {
            // cannot find the user via user name
            return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
        }

        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> changePassword(ChangePasswordRequest request) {
        //createHeaders(username, password)
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        ResponseEntity<String> rawResponse = changePasswordApiCall(request);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (dtoResponse.isSuccessResponse()) {
            return UserDto.parseResponse(rawResponse.getBody());
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> addUser(AddUserRequest request) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.CREATED);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };

        ResponseEntity<String> rawResponse = createUserApiCall(request);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (dtoResponse.isSuccessResponse()) {
            GeneralDtoResponse<UserDto> returnResponse = UserDto.parseResponse(rawResponse.getBody());
            // should we assign group?
            if (request.isSelfManageable()) {
                return makeUserSelfManageable(returnResponse.getData().getUserName());
            }
            return returnResponse;
        }
        return new GeneralDtoResponse<UserDto>(dtoResponse.getError());
    }

    @Override
    public GeneralDtoResponse<UserDto> makeUserSelfManageable(String userNameWithDomain) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        String roleName = wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole();
        // 1. search for group
        GeneralDtoResponse<GetGroupsDto> searchGroup = searchForGroup(roleName);
        // 2. search for user
        GeneralDtoResponse<UserDto> searchUser = searchUser(userNameWithDomain);
        // 3. if both succeed, do the assign
        if (searchGroup.isSuccessResponse() &&
                searchUser.isSuccessResponse()) {
            String groupId = "";
            if (searchGroup.getData().hasData()) {
                groupId = searchGroup.getData().getGroups().get(0).getId();
            } else {
                groupId = wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRoleDefaultId();
            }
            if (StringUtil.isNullOrBlank(groupId)) {
                return new GeneralDtoResponse<>(ErrorCodes.NotFound.description);
            }
            String userId = searchUser.getData().getId();
            String displayName = searchUser.getData().getUserName();
            GeneralDtoResponse<GroupDto> assignToGroup;
            ResponseEntity<String> rawResponse = assignUserToGroupApiCall(groupId, userId, displayName);
            GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
            if (dtoResponse.isSuccessResponse()) {
                assignToGroup = GroupDto.parseResponse(rawResponse.getBody());
            } else {
                assignToGroup = new GeneralDtoResponse<GroupDto> (dtoResponse.getError());
            }
            if (assignToGroup == null || !assignToGroup.isSuccessResponse()) {
                return new GeneralDtoResponse<UserDto>(assignToGroup.getError());
            }
        } else if (!searchGroup.isSuccessResponse()) {
            return new GeneralDtoResponse<>(searchGroup.getError());
        } else if (!searchUser.isSuccessResponse()) {
            return new GeneralDtoResponse<>(searchUser.getError());
        }
        return new GeneralDtoResponse<UserDto>(ErrorCodes.DefaultErrorFromRestClient.description);
    }

    @Override
    public GeneralDtoResponse<UserDto> assignUserToGroup(String userNameWithDomain, String groupName) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        // 1. search for group
        GeneralDtoResponse<GetGroupsDto> searchGroup = searchForGroup(groupName);
        // 2. search for user
        GeneralDtoResponse<UserDto> searchUser = searchUser(userNameWithDomain);
        // 3. if both succeed, do the assign
        if (searchGroup.isSuccessResponse() && searchGroup.getData().hasData() &&
                searchUser.isSuccessResponse()) {
            String groupId = searchGroup.getData().getGroups().get(0).getId();
            String userId = searchUser.getData().getId();
            String displayName = searchUser.getData().getUserName();
            GeneralDtoResponse<GroupDto> assignToGroup;
            ResponseEntity<String> rawResponse = assignUserToGroupApiCall(groupId, userId, displayName);
            GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
            if (dtoResponse.isSuccessResponse()) {
                assignToGroup = GroupDto.parseResponse(rawResponse.getBody());
            } else {
                assignToGroup = new GeneralDtoResponse<GroupDto> (dtoResponse.getError());
            }
            if (assignToGroup == null || !assignToGroup.isSuccessResponse()) {
                return new GeneralDtoResponse<UserDto>(assignToGroup.getError());
            }
        } else if (searchGroup.isSuccessResponse() && !searchGroup.getData().hasData()) {
            return new GeneralDtoResponse<>(ErrorCodes.EmptyResponseFromRestClient.description);
        } else if (!searchGroup.isSuccessResponse()) {
            return new GeneralDtoResponse<>(searchGroup.getError());
        } else if (!searchUser.isSuccessResponse()) {
            return new GeneralDtoResponse<>(searchUser.getError());
        }
        return new GeneralDtoResponse<UserDto>(ErrorCodes.DefaultErrorFromRestClient.description);
    }

    @Override
    public boolean adminDeleteUser(String userNameWithDomain) {
        GeneralDtoResponse<UserDto> searchUser = searchUser(userNameWithDomain);
        if (!searchUser.isSuccessResponse())
            return false;
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.NO_CONTENT);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        ResponseEntity<String> rawResponse = deleteUserApiCall(searchUser.getData().getId());
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        return dtoResponse.isSuccessResponse();
    }

    public GeneralDtoResponse<GetUserListDto> getUsers() {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        ResponseEntity<String> rawResponse = getUsersApiCall();
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (dtoResponse.isSuccessResponse()) {
            return GetUserListDto.parseResponse(rawResponse.getBody());
        }
        return new GeneralDtoResponse<GetUserListDto>(dtoResponse.getError());
    }

    private GeneralDtoResponse<GetGroupsDto> searchForGroup(String groupName) {
        IApiResponseHandler handler = new IApiResponseHandler() {
            @Override
            public boolean isDtoResponseSucceeded(ResponseEntity<String> rawResponse) {
                return (rawResponse.getStatusCode() == HttpStatus.OK);
            }

            @Override
            public String getErrorMessage(ResponseEntity<String> rawResponse) {
                return getErrorMessageFromRawResponse(rawResponse);
            }
        };
        ResponseEntity<String> rawResponse = searchForGroupApiCall(groupName);
        GeneralDtoResponse<String> dtoResponse = handleResponse(handler, rawResponse);
        if (dtoResponse.isSuccessResponse()) {
            return GetGroupsDto.parseResponse(rawResponse.getBody());
        }
        return new GeneralDtoResponse<GetGroupsDto>(dtoResponse.getError());
    }
    // endregion

    // region api call functions
    private ResponseEntity<String> searchForGroupApiCall(String groupName) {
        String url = getSearchGroupRestApiUrl(groupName);

        try {
            return restTemplateUtil.get(url, String.class);
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

    private ResponseEntity<String> assignUserToGroupApiCall(String groupId, String userId, String displayName) {
        String url = getGroupPatchUpdateRestApiUrl(groupId);
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        try {
            UpdateGroupPatchDto dto = UpdateGroupPatchDto.BuildForAssignUserRole(displayName, userId);
            return restTemplateUtil.patch(url, dto, String.class);
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
        String url = getUpdateMePatchUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        try {
            String userName = request.getUserNameWithDomain();
            UpdateUserPatchDto dto = UpdateUserPatchDto.BuildForResetPassword(request.getNewPassword());
            ResponseEntity<String> response = restTemplateUtil.patchWithAuthHeader(url, userName, request.getCurrentPassword(), dto, String.class);
            return response;
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

    private ResponseEntity<String> deleteUserApiCall(String userId) {
        String url = getDeleteUserRestApiUrl(userId);
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        try {
            ResponseEntity<String> response = restTemplateUtil.delete(url, String.class);
            return response;
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
    private ResponseEntity<String> getUsersApiCall() {
        String url = getGetUsersRestApiUrl();
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        try {
            return restTemplateUtil.get(url, String.class);
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

    private ResponseEntity<String> addOrUpdateUserEmailApiCall(String userId, String email, boolean isUpdate) {
        String url = getUpdateUserPatchRestApiUrl(userId);
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        try {
            UpdateUserPatchDto dto = UpdateUserPatchDto.BuildForUpdateEmail(email, isUpdate);
            ResponseEntity<String> response = restTemplateUtil.patch(url, dto, String.class);
            return response;
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

    private ResponseEntity<String> resetPasswordApiCall(String userId, String newPassword) {
        String url = getUpdateUserPatchRestApiUrl(userId);
        if (!isEndpointValid(url)) {
            logError("Invalid url: " +  url);
            return null;
        }
        try {
            UpdateUserPatchDto dto = UpdateUserPatchDto.BuildForResetPassword(newPassword);
            ResponseEntity<String> response = restTemplateUtil.patch(url, dto, String.class);
            return response;
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

    private ResponseEntity<String> searchUserApiCall(String userNameWithDomain) {
        String url = getSearchUserWithUserNameRestApiUrl(userNameWithDomain);

        IdentityServerUserManagerConfig config = wso2Config.getCurrentInstanceConfig().getUserManagement();
        if (config == null || !config.isLoaded()){
            logError("missing config");
            return null;
        }
        try {
            return restTemplateUtil.get(url, String.class);
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

    private ResponseEntity<String> createUserApiCall(AddUserRequest request) {
        String url = getAddUserRestApiUrl();
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
            return restTemplateUtil.post(url, new co.za.flash.credential.management.dto.request.rest.UserDto(request, config.getDomain()), String.class);
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
    // endregion

    // region url maker functions
    private String getGetUsersRestApiUrl() {
        return getAddUserRestApiUrl();
    }

    private String getGroupPatchUpdateRestApiUrl(String groupId) {
        String configCategoryKey = "group";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        return StringUtil.joinUrl(baseUrl, pathUrl, groupId);
    }

    private String getSearchGroupRestApiUrl(String groupName) {
        String configCategoryKey = "group";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        String url = StringUtil.joinUrl(baseUrl, pathUrl, "");
        // append filter
        String filter = "?filter=displayName eq " + groupName;
        return url + filter;
    }

    private String getSearchUserWithUserNameRestApiUrl(String userNameWithDomain) {
        String configCategoryKey = "user";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        String url = StringUtil.joinUrl(baseUrl, pathUrl, "");
        // append filter
        String filter = "?filter=userName eq " + userNameWithDomain;
        return url + filter;
    }

    private String getUpdateUserPatchRestApiUrl(String userId) {
        String configCategoryKey = "user";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        return StringUtil.joinUrl(baseUrl, pathUrl, userId);
    }

    private String getAddUserRestApiUrl() {
        String configCategoryKey = "user";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        return StringUtil.joinUrl(baseUrl, pathUrl, "");
    }

    private String getDeleteUserRestApiUrl(String userId) {
        String configCategoryKey = "user";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        return StringUtil.joinUrl(baseUrl, pathUrl, userId);
    }

    private String getUpdateMePatchUrl() {
        String configCategoryKey = "me";
        String baseUrl = wso2Config.getCurrentInstanceConfig().getUrl().getBase();
        String pathUrl = wso2Config.getCurrentInstanceConfig().getUrl().getCategories().get(configCategoryKey).getScim2();
        return StringUtil.joinUrl(baseUrl, pathUrl, "");
    }
    // endregion

    private ResponseEntity<String> getValidErrorFromHttpClientErrorException(HttpClientErrorException hcee) {
        return ResponseEntity.status(hcee.getStatusCode()).body(hcee.getMessage());
    }

    private ResponseEntity<String> getValidErrorFromException(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    private String getErrorMessageFromRawResponse(ResponseEntity<String> rawResponse) {
        ErrorDto error = ErrorDto.parseJson(rawResponse.getBody());
        if (error == null)
            error = new ErrorDto(rawResponse.getStatusCode().toString(), rawResponse.getBody());
        return error.getStatus();
    }

    @Override
    public void logError(String errorMessage) {
        logger.error(errorMessage);
    }
}
