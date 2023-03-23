package co.za.flash.credential.management.dto.request.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderMapDto {
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;
    @JsonProperty("primary")
    private boolean primary;

    public OrderMapDto(String type, String value, boolean primary) {
        this.type = type;
        this.value = value;
        this.primary = primary;
    }
}
