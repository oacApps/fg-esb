package co.za.flash.esb.ricaregistration.dto.request;

import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String suburb;
    private String town;
    private String postalCode;
    private String countryCode;
}
