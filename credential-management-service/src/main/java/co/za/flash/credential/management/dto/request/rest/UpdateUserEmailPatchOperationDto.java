package co.za.flash.credential.management.dto.request.rest;

import co.za.flash.credential.management.helper.interfaces.IUserPatchOperationDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserEmailPatchOperationDto implements IUserPatchOperationDto {
    @JsonProperty("op")
    private String op;
    @JsonProperty("value")
    private UpdateUserEmailValuePatchOperationDto value;

    private UpdateUserEmailPatchOperationDto(String op, UpdateUserEmailValuePatchOperationDto value) {
        this.op = op;
        this.value = value;
    }

    public static UpdateUserEmailPatchOperationDto BuildForAddEmail(String email) {
        return new UpdateUserEmailPatchOperationDto("add", new UpdateUserEmailValuePatchOperationDto(email));
    }

    public static UpdateUserEmailPatchOperationDto BuildForUpdateEmail(String email) {
        return new UpdateUserEmailPatchOperationDto("replace", new UpdateUserEmailValuePatchOperationDto(email));
    }
}
