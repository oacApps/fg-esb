package co.za.flash.credential.management.dto.request.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateGroupPatchOperationDto {
    @JsonProperty("op")
    private String op;
    @JsonProperty("value")
    private GroupMembersDto value;

    public UpdateGroupPatchOperationDto(String op, GroupMembersDto value) {
        this.op = op;
        this.value = value;
    }

    public static UpdateGroupPatchOperationDto BuildForAssignUserRole(String userName, String userId) {
        return new UpdateGroupPatchOperationDto("add", new GroupMembersDto(new MemberDto(userName, userId)));
    }
}
