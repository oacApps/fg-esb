package co.za.flash.esb.ricaregistration.dto.request;

import lombok.Data;

@Data
public class RegistrationsRequestDTO {
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
