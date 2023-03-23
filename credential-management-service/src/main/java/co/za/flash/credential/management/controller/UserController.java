package co.za.flash.credential.management.controller;

import co.za.flash.credential.management.config.WSO2Config;
import co.za.flash.credential.management.dto.response.UserDto;
import co.za.flash.credential.management.dto.response.GeneralDtoResponse;
import co.za.flash.credential.management.dto.response.GetUserListDto;
import co.za.flash.credential.management.helper.enums.ErrorCodes;
import co.za.flash.credential.management.helper.enums.PasswordValidationRules;
import co.za.flash.credential.management.helper.interfaces.IUserService;
import co.za.flash.credential.management.helper.utils.JwtTokenUtil;
import co.za.flash.credential.management.helper.utils.PasswordValidator;
import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.helper.utils.TokenCredentialHelper;
import co.za.flash.credential.management.model.request.AddUserRequest;
import co.za.flash.credential.management.model.request.ChangePasswordRequest;
import co.za.flash.credential.management.model.request.ResetPasswordRequest;
import co.za.flash.credential.management.model.response.ErrorResponse;
import co.za.flash.credential.management.model.response.UserResponse;
import co.za.flash.credential.management.model.response.GeneralResponse;
import co.za.flash.credential.management.restclient.UserRestApiService;
import co.za.flash.credential.management.restclient.UserSoapApiService;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static co.za.flash.credential.management.helper.utils.JwtTokenUtil.domainClaimFieldKey;

@RestController
@RequestMapping("user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WSO2Config wso2Config;

    @Autowired
    UserRestApiService scim2Client;

    @Autowired
    UserSoapApiService soapClient;

    @GetMapping(
            value = "/connectionCheck",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<Boolean>> connectionCheck() {
        String configCategoryKey = "user";
        // check input
        GeneralResponse<Boolean> response;
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
                !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            if (wso2Config == null ) {
                response = new GeneralResponse(ErrorCodes.WrongWso2Configuration, "wso2Config == null ");
            } else if (!wso2Config.isLoaded() ) {
                response = new GeneralResponse(ErrorCodes.WrongWso2Configuration, "!wso2Config.isLoaded() ");
            } else if (wso2Config.getCurrentInstanceConfig() == null ) {
                response = new GeneralResponse(ErrorCodes.WrongWso2Configuration, "wso2Config.getCurrentInstanceConfig() == null");
            } else if (!wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey) ) {
                response = new GeneralResponse(ErrorCodes.WrongWso2Configuration, "!wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey) ");
            } else {
                response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            }
            return ResponseEntity.ok(response);
        }
        String token = new TokenCredentialHelper().getCurrentCredential(wso2Config);
        if (StringUtil.isNullOrBlank(token)) {
            response = new GeneralResponse(ErrorCodes.Default, "Credential is empty");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(new GeneralResponse(true));
    }

    @PostMapping(
            value = "/addUser",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<UserResponse>> addUser(
            @RequestBody AddUserRequest request, @RequestHeader Map<String, String> headers) {
        String configCategoryKey = "user";

        // check input
        GeneralResponse<UserResponse> response;
        if (!request.isValidInput()) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
            !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            return ResponseEntity.ok(response);
        }
        // validate password before call identity server
        if (request.isValidatePassword()) {
            ErrorResponse errorResponse = validatePassword(request.getPassword(), request.getUsername());
            if (errorResponse != null) {
                response = new GeneralResponse(errorResponse);
                return ResponseEntity.ok(response);
            }
        }
        // #61040 get domain name out from token
        String domainName = "";
        if (headers != null) {
            try {
                Map<String, Object> claimsSet = JwtTokenUtil.getJWTClaims(headers);
                if (claimsSet.keySet().contains(domainClaimFieldKey)) {
                    domainName = claimsSet.get(domainClaimFieldKey).toString();
                }
            } catch (Throwable t) {
                String errorMessage = t.getClass().toString() + ": " + t.getLocalizedMessage();
                response = new GeneralResponse(ErrorCodes.InvalidDomain, errorMessage);
                return ResponseEntity.ok(response);
            }
            if (StringUtil.isNullOrBlank(domainName)) {
                response = new GeneralResponse(ErrorCodes.MissingDomain);
                return ResponseEntity.ok(response);
            } else {
                request.setDomain(domainName);
            }
        }

        IUserService client = getRestApiService();
        GeneralDtoResponse<UserDto> dtoResponse = client.addUser(request);
        if (dtoResponse.isSuccessResponse() && dtoResponse.getData().isValid()) {
            UserResponse returningResponse = new UserResponse(dtoResponse.getData(),
                    wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole());
            response = new GeneralResponse<UserResponse>(returningResponse);
        } else {
            String errorMessage = dtoResponse.getError();
            String referenceNumber = dtoResponse.getErrorReference();
            response = new GeneralResponse<UserResponse>(ErrorCodes.Default, errorMessage, referenceNumber);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            value = "/makeUserSelfManageable",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<UserResponse>> makeUserSelfManageable(
            @RequestParam String userName, @RequestHeader Map<String, String> headers) {
        String configCategoryKey = "user";
        // check input
        GeneralResponse<UserResponse> response;
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
                !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            return ResponseEntity.ok(response);
        }
        if (StringUtil.isNullOrBlank(userName)) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }
        // #61040 get domain name out from token
        String domainName = "";
        String userNameWithDomain = userName;
        if (headers != null) {
            try {
                Map<String, Object> claimsSet = JwtTokenUtil.getJWTClaims(headers);
                if (claimsSet.keySet().contains(domainClaimFieldKey)) {
                    domainName = claimsSet.get(domainClaimFieldKey).toString();
                }
            } catch (Throwable t) {
                String errorMessage = t.getClass().toString() + ": " + t.getLocalizedMessage();
                response = new GeneralResponse(ErrorCodes.InvalidDomain, errorMessage);
                return ResponseEntity.ok(response);
            }
            if (StringUtil.isNullOrBlank(domainName)) {
                response = new GeneralResponse(ErrorCodes.MissingDomain);
                return ResponseEntity.ok(response);
            } else {
                // correct and expected
                userNameWithDomain = domainName + "/" + userName;
            }
        }

        IUserService client = getRestApiService();
        // #61936 check if username exists first
        GeneralDtoResponse<UserDto> dtoResponseUserCheck = client.searchUser(userNameWithDomain);
        if (!(dtoResponseUserCheck.isSuccessResponse() && dtoResponseUserCheck.getData().isValid())) {
            String errorMessage = dtoResponseUserCheck.getError();
            response = new GeneralResponse(ErrorCodes.Default, errorMessage);
            return ResponseEntity.ok(response);
        }
        String roleName = wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole();
        UserResponse tempResponse = new UserResponse(dtoResponseUserCheck.getData(), roleName);
        if (tempResponse.isSelfManageable())
            response = new GeneralResponse<UserResponse>(ErrorCodes.UserExists);
        else {
            // assign user
            GeneralDtoResponse<UserDto> userDto = client.makeUserSelfManageable(userNameWithDomain);
            if (userDto.isSuccessResponse() && userDto.getData().isValid()) {
                UserResponse returningResponse = new UserResponse(userDto.getData(),
                        wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole());
                response = new GeneralResponse<UserResponse>(returningResponse);
            } else {
                String errorMessage = userDto.getError();
                response = new GeneralResponse<UserResponse>(ErrorCodes.Default, errorMessage);
            }
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            value = "/checkPassword",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<Boolean>> checkPassword(@RequestParam String query) {
        // check input
        GeneralResponse<Boolean> response;
        if (StringUtil.isNullOrBlank(query)) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }

        // check if the password matches all the requirements
        PasswordValidationRules checkResult = PasswordValidator.validateWithExclusions(Arrays.asList(query),
                Arrays.asList(PasswordValidationRules.RepetitionWithUsername.code));
        if (checkResult != null) {
            response = new GeneralResponse(ErrorCodes.InvalidPassword, checkResult.getErrorMessage(), checkResult.description);
            return ResponseEntity.ok(response);
        }
        response = new GeneralResponse<Boolean>(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            value = "/getInvalidPasswordExamples",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<List<String>>> getInvalidPasswordExamples(@RequestParam String passwordRuleDescription) {
        // check input
        GeneralResponse<List<String>> response;
        if (StringUtil.isNullOrBlank(passwordRuleDescription)) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }

        PasswordValidationRules instance = PasswordValidationRules.getInstance(passwordRuleDescription);
        if (instance == null) {
            response = new GeneralResponse(ErrorCodes.InvalidPasswordRule);
            return ResponseEntity.ok(response);
        }
        response = new GeneralResponse(instance.getInvalidExamples());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            value = "/admin/deleteUser",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<Boolean>> adminDeleteUser(
            @RequestParam String userName, @RequestHeader Map<String, String> headers) {
        String configCategoryKey = "user";
        // check input
        GeneralResponse<Boolean> response;
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
                !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            return ResponseEntity.ok(response);
        }
        if (StringUtil.isNullOrBlank(userName)) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }
        // #61040 get domain name out from token
        String domainName = "";
        String userNameWithDomain = userName;
        if (headers != null) {
            try {
                Map<String, Object> claimsSet = JwtTokenUtil.getJWTClaims(headers);
                if (claimsSet.keySet().contains(domainClaimFieldKey)) {
                    domainName = claimsSet.get(domainClaimFieldKey).toString();
                }
            } catch (Throwable t) {
                String errorMessage = t.getClass().toString() + ": " + t.getLocalizedMessage();
                response = new GeneralResponse(ErrorCodes.InvalidDomain, errorMessage);
                return ResponseEntity.ok(response);
            }
            if (StringUtil.isNullOrBlank(domainName)) {
                response = new GeneralResponse(ErrorCodes.MissingDomain);
                return ResponseEntity.ok(response);
            } else {
                // correct and expected
                userNameWithDomain = domainName + "/" + userName;
            }
        }
        IUserService client = getRestApiService();
        GeneralDtoResponse<UserDto> dtoResponse = client.searchUser(userNameWithDomain);
        if (dtoResponse.isSuccessResponse() && dtoResponse.getData().isValid()) {
            boolean dtoNewResponse = client.adminDeleteUser(userNameWithDomain);
            return ResponseEntity.ok(new GeneralResponse<Boolean>(dtoNewResponse));
        } else {
            response = new GeneralResponse(ErrorCodes.NotFound);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(
            value = "/searchUser",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<UserResponse>> searchUser(
            @RequestParam String userName, @RequestHeader Map<String, String> headers) {
        String configCategoryKey = "user";
        // check input
        GeneralResponse<UserResponse> response;
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
                !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            return ResponseEntity.ok(response);
        }
        if (StringUtil.isNullOrBlank(userName)) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }
        // #61040 get domain name out from token
        String domainName = "";
        String userNameWithDomain = userName;
        if (headers != null) {
            try {
                Map<String, Object> claimsSet = JwtTokenUtil.getJWTClaims(headers);
                if (claimsSet.keySet().contains(domainClaimFieldKey)) {
                    domainName = claimsSet.get(domainClaimFieldKey).toString();
                }
            } catch (Throwable t) {
                String errorMessage = t.getClass().toString() + ": " + t.getLocalizedMessage();
                response = new GeneralResponse(ErrorCodes.InvalidDomain, errorMessage);
                return ResponseEntity.ok(response);
            }
            if (StringUtil.isNullOrBlank(domainName)) {
                response = new GeneralResponse(ErrorCodes.MissingDomain);
                return ResponseEntity.ok(response);
            } else {
                // correct and expected
                userNameWithDomain = domainName + "/" + userName;
            }
        }

        IUserService client = getRestApiService();
        GeneralDtoResponse<UserDto> dtoResponse = client.searchUser(userNameWithDomain);
        if (dtoResponse.isSuccessResponse() && dtoResponse.getData().isValid()) {
            UserResponse returningResponse = new UserResponse(dtoResponse.getData(),
                    wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole());
            response = new GeneralResponse<UserResponse>(returningResponse);
        } else {
            String errorMessage = dtoResponse.getError();
            response = new GeneralResponse<UserResponse>(ErrorCodes.Default, errorMessage);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/admin/resetPassword",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<UserResponse>> adminResetPassword(
            @RequestBody ResetPasswordRequest request, @RequestHeader Map<String, String> headers) {
        String configCategoryKey = "user";

        // check input
        GeneralResponse<UserResponse> response;
        if (!request.isValidInput()) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
                !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            return ResponseEntity.ok(response);
        }
        // validate password before call identity server
        if (request.isValidatePassword()) {
            // do not check username for the username is unknown
            PasswordValidationRules failedCase = PasswordValidator.validateWithExclusions(Arrays.asList(request.getNewPassword()),
                    Arrays.asList(PasswordValidationRules.RepetitionWithUsername.code));
            if (failedCase != null) {
                response = new GeneralResponse(ErrorCodes.InvalidPassword, failedCase.getErrorMessage(), failedCase.description);
                return ResponseEntity.ok(response);
            }
        }
        // #61040 get domain name out from token
        String domainName = "";
        String userNameWithDomain = request.getUserName();
        if (headers != null) {
            try {
                Map<String, Object> claimsSet = JwtTokenUtil.getJWTClaims(headers);
                if (claimsSet.keySet().contains(domainClaimFieldKey)) {
                    domainName = claimsSet.get(domainClaimFieldKey).toString();
                }
            } catch (Throwable t) {
                String errorMessage = t.getClass().toString() + ": " + t.getLocalizedMessage();
                response = new GeneralResponse(ErrorCodes.InvalidDomain, errorMessage);
                return ResponseEntity.ok(response);
            }
            if (StringUtil.isNullOrBlank(domainName)) {
                response = new GeneralResponse(ErrorCodes.MissingDomain);
                return ResponseEntity.ok(response);
            } else {
                userNameWithDomain = domainName + "/" + request.getUserName();
                request.setUserNameWithDomain(userNameWithDomain);
            }
        }

        IUserService client = getRestApiService();
        GeneralDtoResponse<UserDto> dtoResponse = client.adminResetPassword(request);
        if (dtoResponse.isSuccessResponse() && dtoResponse.getData().isValid()) {
            UserResponse returningResponse = new UserResponse(dtoResponse.getData(),
                    wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole());
            response = new GeneralResponse<UserResponse>(returningResponse);
        } else {
            String errorMessage = dtoResponse.getError();
            response = new GeneralResponse<UserResponse>(ErrorCodes.Default, errorMessage);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            value = "/changePassword",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<GeneralResponse<UserResponse>> changePassword(
            @RequestBody ChangePasswordRequest request, @RequestHeader Map<String, String> headers) {
        String configCategoryKey = "user";

        // check input
        GeneralResponse<UserResponse> response;
        if (!request.isValidInput()) {
            response = new GeneralResponse(ErrorCodes.WrongInput);
            return ResponseEntity.ok(response);
        }
        // check config
        if (wso2Config == null || !wso2Config.isLoaded() ||
                wso2Config.getCurrentInstanceConfig() == null ||
                !wso2Config.getCurrentInstanceConfig().getUrl().getCategories().containsKey(configCategoryKey)) {
            response = new GeneralResponse(ErrorCodes.WrongWso2Configuration);
            return ResponseEntity.ok(response);
        }

        // validate password before call identity server
        if (request.isValidatePassword()) {
            ErrorResponse errorResponse = validatePassword(request.getNewPassword(), request.getUserName());
            if (errorResponse != null) {
                response = new GeneralResponse(errorResponse);
                return ResponseEntity.ok(response);
            }
        }

        String domainName = "";
        String userNameWithDomain = request.getUserName();
        if (headers != null) {
            try {
                Map<String, Object> claimsSet = JwtTokenUtil.getJWTClaims(headers);
                if (claimsSet.keySet().contains(domainClaimFieldKey)) {
                    domainName = claimsSet.get(domainClaimFieldKey).toString();
                }
            } catch (Throwable t) {
                String errorMessage = t.getClass().toString() + ": " + t.getLocalizedMessage();
                response = new GeneralResponse(ErrorCodes.InvalidDomain, errorMessage);
                return ResponseEntity.ok(response);
            }
            if (StringUtil.isNullOrBlank(domainName)) {
                response = new GeneralResponse(ErrorCodes.MissingDomain);
                return ResponseEntity.ok(response);
            } else {
                userNameWithDomain = domainName + "/" + request.getUserName();
                request.setUserNameWithDomain(userNameWithDomain);
            }
        }

        IUserService client = getRestApiService();
        GeneralDtoResponse<UserDto> dtoResponseUserCheck = client.searchUser(userNameWithDomain);
        if (!(dtoResponseUserCheck.isSuccessResponse() && dtoResponseUserCheck.getData().isValid())) {
            String errorMessage = dtoResponseUserCheck.getError();
            response = new GeneralResponse(ErrorCodes.Default, errorMessage);
            return ResponseEntity.ok(response);
        }
        String roleName = wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole();
        UserResponse tempResponse = new UserResponse(dtoResponseUserCheck.getData(), roleName);

        if (!tempResponse.isSelfManageable()) {
            response = new GeneralResponse<UserResponse>(ErrorCodes.UserCannotChangePassword);
        } else {
            GeneralDtoResponse<UserDto> userDtoResponse = client.changePassword(request);
            if (userDtoResponse.isSuccessResponse() && userDtoResponse.getData().isValid()) {
                UserResponse returningResponse = new UserResponse(userDtoResponse.getData(),
                        wso2Config.getCurrentInstanceConfig().getUserManagement().getProfileRole());
                response = new GeneralResponse<UserResponse>(returningResponse);
            } else {
                String errorMessage = userDtoResponse.getError();
                response = new GeneralResponse<UserResponse>(ErrorCodes.Default, errorMessage);
            }
        }
        return ResponseEntity.ok(response);
    }

    private ErrorResponse validatePassword(@NotNull String password, @NotNull String username) {
        PasswordValidationRules failedCase = PasswordValidator.validateAllRules(Arrays.asList(password, username));
        if (failedCase != null) {
            return ErrorResponse.Builder.build(ErrorCodes.InvalidPassword, failedCase.getErrorMessage(), failedCase.description);
        }
        return null;
    }

    private IUserService getRestApiService() {
        if (wso2Config.isLoaded() && wso2Config.usingRestApi())
            return scim2Client;
        return soapClient;
    }
}
