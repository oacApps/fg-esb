package co.za.flash.credential.management.dto.request.rest;

import co.za.flash.credential.management.helper.interfaces.IUserPatchOperationDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class UpdateUserPatchDto {
    /*{
        "schemas":
       ["urn:ietf:params:scim:api:messages:2.0:PatchOp"],
        "Operations":[{
        "op":"replace",
                "path":"password",
                "value":"password1"
    }]
    }*/
    @JsonProperty("Operations")
    private ArrayList<IUserPatchOperationDto> operations = new ArrayList<>();

    private UpdateUserPatchDto(IUserPatchOperationDto operation) {
        operations.clear();
        operations.add(operation);
    }

    public static UpdateUserPatchDto BuildForResetPassword(String newPassword) {
        return new UpdateUserPatchDto(UpdateUserPatchOperationDto.BuildForResetPassword(newPassword));
    }

    public static UpdateUserPatchDto BuildForUpdateEmail(String email, boolean isUpdate) {
        if (isUpdate)
            return new UpdateUserPatchDto(UpdateUserEmailPatchOperationDto.BuildForUpdateEmail(email));
        else
            return new UpdateUserPatchDto(UpdateUserEmailPatchOperationDto.BuildForAddEmail(email));
    }
}
