package co.za.flash.credential.management.helper.interfaces;

import co.za.flash.credential.management.dto.response.GetUserListDto;
import co.za.flash.credential.management.dto.response.UserDto;
import co.za.flash.credential.management.dto.response.GeneralDtoResponse;
import co.za.flash.credential.management.model.request.AddUserRequest;
import co.za.flash.credential.management.model.request.ChangePasswordRequest;
import co.za.flash.credential.management.model.request.ResetPasswordRequest;

public interface IUserService {
    public GeneralDtoResponse<UserDto> addUser(AddUserRequest request);
    public GeneralDtoResponse<UserDto> makeUserSelfManageable(String userNameWithDomain);
    public GeneralDtoResponse<UserDto> assignUserToGroup(String userNameWithDomain, String groupName);
    public GeneralDtoResponse<UserDto> adminResetPassword(ResetPasswordRequest request);
    public boolean adminDeleteUser(String userNameWithDomain);
    public GeneralDtoResponse<UserDto> changePassword(ChangePasswordRequest request);
    public GeneralDtoResponse<UserDto> searchUser(String userNameWithDomain);
}
