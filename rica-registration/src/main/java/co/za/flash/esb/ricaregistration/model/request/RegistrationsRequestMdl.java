package co.za.flash.esb.ricaregistration.model.request;

import co.za.flash.esb.ricaregistration.dto.request.AddressDTO;
import co.za.flash.esb.ricaregistration.dto.request.IdInfo;
import lombok.Data;

@Data
public class RegistrationsRequestMdl {
    private String msisdn;
    private String subscriberNetwork;
    private String existingSubscriber;
    private String referenceType;
    private String referenceNumber;
    private String idType;
    private String firstName;
    private String lastName;

    private IdInfo idInfo;
    private AddressDTO address;
    private boolean proofOfAddress;
}
