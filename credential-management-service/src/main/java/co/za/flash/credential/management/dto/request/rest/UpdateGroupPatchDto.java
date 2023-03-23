package co.za.flash.credential.management.dto.request.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateGroupPatchDto {
    @JsonProperty("Operations")
    private List<UpdateGroupPatchOperationDto> operations;

    public UpdateGroupPatchDto(List<UpdateGroupPatchOperationDto> operations) {
        this.operations = operations;
    }

    public UpdateGroupPatchDto(UpdateGroupPatchOperationDto operation) {
        this.operations = List.of(operation);
    }

    public static UpdateGroupPatchDto BuildForAssignUserRole(String userName, String userId) {
        return new UpdateGroupPatchDto(UpdateGroupPatchOperationDto.BuildForAssignUserRole(userName, userId));
    }
}
