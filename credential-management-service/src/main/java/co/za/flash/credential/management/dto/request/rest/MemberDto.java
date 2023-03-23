package co.za.flash.credential.management.dto.request.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberDto {
    @JsonProperty("display")
    private String display;
    @JsonProperty("value")
    private String value;

    public MemberDto(String display, String value) {
        this.display = display;
        this.value = value;
    }
}
