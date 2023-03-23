package co.za.flash.credential.management.dto.request.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateUserEmailValuePatchOperationDto {
    @JsonProperty("emails")
    private List<String> emails;

    public UpdateUserEmailValuePatchOperationDto(String email) {
        this.emails = List.of(email);
    }
}
