package co.za.flash.esb.cellular.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class CellularPinResponseDTO implements Serializable {
    private long _version;
    private long transactionID;
    private long responseCode;
    private String responseMessage;
    private BigInteger transactionNumber;
    private String rechargeResponseType;
    private TokenArrDTO tokens;
    private String expiryDate;
    private String dataSize;
    private long callCentre;
    private String referenceId;
}
