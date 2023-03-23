package co.za.flash.esb.ricaregistration.dto.response;

import co.za.flash.esb.ricaregistration.dto.request.AddressDTO;
import co.za.flash.esb.ricaregistration.dto.request.IdInfo;
import lombok.Data;

@Data
public class RegistrationsResponseDTO {
    private String actionCode;
    private String errorMessage;
}
