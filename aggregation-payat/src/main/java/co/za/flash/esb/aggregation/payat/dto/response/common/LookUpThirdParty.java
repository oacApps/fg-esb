package co.za.flash.esb.aggregation.payat.dto.response.common;

import lombok.Data;

@Data
public class LookUpThirdParty extends ThirdPartyMsgComm {
    private CustomerData CustomerData;
    private IssuerData IssuerData;
    private PaymentRules PaymentRules;
}
