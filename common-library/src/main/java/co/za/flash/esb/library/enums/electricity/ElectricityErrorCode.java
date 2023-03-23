package co.za.flash.esb.library.enums.electricity;

public enum ElectricityErrorCode {
    NO_ERROR (0, "NO ERROR"),
    OBS_SYSTEM_ERROR(6, "OBS SYSTEM ERROR"),
    MESSAGE_QUEUE_FULL(10, "MESSAGE QUEUE FULL"),
    NO_ACTIVE_ROUTE(14, "NO ACTIVE ROUTE"),
    THROTTLING_ERROR(22, "THROTTLING ERROR"),
    TENDER_AMOUNT_SMALL(54, "TENDER AMOUNT IS TOO SMALL OR LESS THAN REQUIRED"),
    PREPAYMENT_SALE_NOT_FINALISED (55, "THE PREPAYMENT SALE HAS NOT BEEN FINALISED YET"),
    AMOUNT_LESS_OUTSTANDING_ARREARS_OR_DUE(57, "AMOUNT IS LESS THAN THE OUTSTANDING ARREARS/DUE AMOUNT"),
    INVALID_METER_ID(63, "INVALID METER ID"),
    REQUEST_NOT_SUPPORTED_FOR_METER(66, "THE REQUEST IS NOT SUPPORTED FOR THIS METER"),
    PURCHASE_AMOUNT_BELOW_RECEIVER_MINIMUM(68, "THE PURCHASE AMOUNT IS BELOW RECEIVER’S MINIMUM"),
    METER_NOT_REGISTERED(70, "METER IS NOT REGISTERED"),
    SALES_TO_METER_BLOCKED(71, "SALES TO THIS METER IS BLOCKED"),
    FREE_BASIC_ELECTRICITY_ALREADY_ISSUED(73, "THE FREE BASIC ELECTRICITY IS ALREADY BEEN ISSUED"),
    SERVICE_REQUEST_CONTAINS_ELEMENTS_WITH_INVALID_VALUES(76, "THE SERVICE REQUEST CONTAINS ELEMENTS WITH INVALID VALUES"),
    THIRD_PARTY_SERVICE_DOWN(77, "THE THIRD PARTY SERVICE IS DOWN"),
    TENDER_AMOUNT_TOO_HIGH(79, "TENDER AMOUNT IS TOO HIGH"),
    STOCK_NOT_AVAILABLE_AT_THIRD_PARTY(80, "THE STOCK IS NOT AVAILABLE AT THE THIRD PARTY"),
    THIRD_PARTY_SERVICE_RETURNED_TIMEOUT_RESPONSE(82, "THE THIRD PARTY SERVICE HAS RETURNED A TIMEOUT RESPONSE"),
    TRANSACTION_ALREADY_SUBMITTED(83, "THIS TRANSACTION IS ALREADY SUBMITTED"),
    INFO_OUT_DATE_FIRST_UPDATE_METER_KEY(85, "INFO OUT OF DATE, FIRST UPDATE METER KEY"),
    INVALID_STS_METER_DATA(86, "INVALID STS METER DATA (SGC,KRN ARE NOT VALID)"),
    CUSTOMER_ACCOUNT_HAS_ARREARS(87, "THE CUSTOMER ACCOUNT HAS ARREARS"),
    CONSUMER_NOT_ACTIVE_OR_BLOCKED_FROM_BUYING(89, "THIS CONSUMER IS NOT ACTIVE/BLOCKED FROM BUYING"),
    CUSTOMER_NOT_FOUND(90, "THE CUSTOMER IS NOT FOUND"),
    THIRD_PARTY_SERVICE_ERROR(91, "THE THIRD PARTY SERVICE ERROR"),
    METER_NOT_REGISTERED_FOR_RICA(94, "THE METER IS NOT REGISTERED FOR RICA"),
    THIRD_PARTY_INTERNAL_ERROR(96, "THE THIRD PARTY INTERNAL ERROR"),
    INVALID_AMOUNT(103, "INVALID AMOUNT"),
    REQUEST_DOES_NOT_CONTAIN_ALL_REQUIRED_ELEMENTS(106, "THE REQUEST DOES NOT CONTAIN ALL REQUIRED ELEMENTS"),
    NO_SUCCESSFUL_PREVIOUS_VEND_FOUND(131, "NO SUCCESSFUL PREVIOUS VEND FOUND");

    private final int code;
    private final String errorResponse;

    ElectricityErrorCode(int code, String errorResponse) {
        this.code = code;
        this.errorResponse = errorResponse;
    }
    public int getCode(){ return this.code;}

    public String getType() {
        return errorResponse;
    }
}