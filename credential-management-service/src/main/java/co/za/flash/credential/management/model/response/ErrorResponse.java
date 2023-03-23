package co.za.flash.credential.management.model.response;

import co.za.flash.credential.management.helper.enums.ErrorCodes;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ErrorResponse {
    private int errorCode;
    private String errorMessage;
    private String referenceNumber;

    private ErrorResponse(int errorCode,
     String errorMessage,
     String referenceNumber) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.referenceNumber = referenceNumber;
    }

    @JsonIgnore
    public boolean isValid(){
        return errorCode > 0;
    }

    public static class Builder {
        public static ErrorResponse build(ErrorCodes errorCode) {
            return new ErrorResponse(errorCode.code, errorCode.description, "");
        }

        public static ErrorResponse build(ErrorCodes errorCode, String message) {
            if (StringUtil.isNullOrBlank(message)) {
                return new ErrorResponse(errorCode.code, errorCode.description, "");
            }
            return new ErrorResponse(errorCode.code, message, "");
        }

        public static ErrorResponse build(ErrorCodes errorCode, String message, String referenceNumber) {
            if (StringUtil.isNullOrBlank(message)) {
                return new ErrorResponse(errorCode.code, errorCode.description, referenceNumber);
            }
            return new ErrorResponse(errorCode.code, message, referenceNumber);
        }
    }
}
