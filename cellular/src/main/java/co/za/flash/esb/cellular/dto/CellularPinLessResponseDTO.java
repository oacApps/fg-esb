package co.za.flash.esb.cellular.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Data
public class CellularPinLessResponseDTO {
    private long _version;
    private long transactionID;
    private long responseCode;
    private String responseMessage;
    private BigInteger transactionNumber;
    private String rechargeResponseType;
    private String dataSize;
    private long callCentre;
    private String referenceId;
}
