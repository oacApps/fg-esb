package co.za.flash.credential.management.dto.request.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GroupMembersDto {
    @JsonProperty("members")
    private List<MemberDto> members;

    public GroupMembersDto(List<MemberDto> members) {
        this.members = members;
    }

    public GroupMembersDto(MemberDto member) {
        this.members = List.of(member);
    }
}
