package co.za.flash.credential.management.dto.request.rest;

import co.za.flash.credential.management.helper.interfaces.IUserPatchOperationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
public class UpdateUserPatchOperationDto implements IUserPatchOperationDto {
    @JsonProperty("op")
    private String op;
    @JsonProperty("path")
    private String path;
    @JsonProperty("value")
    private String value;

    private UpdateUserPatchOperationDto(String op, String path, String value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public static UpdateUserPatchOperationDto BuildForResetPassword(String newPassword) {
        String op = "replace";
        String path = "password";
        String value = newPassword;
        return new UpdateUserPatchOperationDto(op, path, value);
    }
}
