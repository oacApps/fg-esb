package co.za.flash.credential.management.model.response;

import co.za.flash.credential.management.dto.response.GeneralDtoResponse;
import co.za.flash.credential.management.helper.enums.ErrorCodes;
import co.za.flash.credential.management.helper.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GeneralResponse<T> {
    @JsonProperty("error")
    private ErrorResponse error;
    @JsonProperty("data")
    private T data;

    public GeneralResponse (T data) {
        this.error = null;
        this.data = data;
    }

    public GeneralResponse (ErrorResponse error) {
        this.error = error;
        this.data = null;
    }

    public GeneralResponse (GeneralDtoResponse<T> dtoResponse) {
        if (dtoResponse == null) {
            this.error = ErrorResponse.Builder.build(ErrorCodes.EmptyResponseFromRestClient);
            this.data = null;
        } else if (!dtoResponse.isValid()) {
            this.error = ErrorResponse.Builder.build(ErrorCodes.InvalidResponseFromRestClient);
            this.data = null;
        } else if (dtoResponse.isSuccessResponse()) {
            this.data = dtoResponse.getData();
            this.error = null;
        } else {
            this.error = ErrorResponse.Builder.build(ErrorCodes.DefaultErrorFromRestClient);
            this.data = null;
        }
    }

    public GeneralResponse (ErrorCodes code) {
        this.error = ErrorResponse.Builder.build(code);
        this.data = null;
    }

    public GeneralResponse (ErrorCodes code, String message) {
        this.error = ErrorResponse.Builder.build(code, message);
        this.data = null;
    }

    public GeneralResponse (ErrorCodes code, String message, String referenceNumber) {
        this.error = ErrorResponse.Builder.build(code, message, referenceNumber);
        this.data = null;
    }

    @JsonIgnore
    public boolean isValid() {
        return (error != null && error.isValid()) || isSuccessResponse();
    }

    @JsonIgnore
    public boolean isSuccessResponse() {
        return error == null && data != null;
    }
}
