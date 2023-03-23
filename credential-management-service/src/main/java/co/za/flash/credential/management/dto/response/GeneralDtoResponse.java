package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.utils.StringUtil;
import lombok.Data;

@Data
public class GeneralDtoResponse<T> {
    private String error;
    private String errorReference;
    private T data;

    public GeneralDtoResponse(String message, T data) {
        this.error = message;
        this.errorReference = null;
        this.data = data;
    }

    public GeneralDtoResponse (T data) {
        this.error = null;
        this.errorReference = null;
        this.data = data;
    }

    public GeneralDtoResponse (String message) {
        this.error = message;
        this.errorReference = null;
        this.data = null;
    }

    public GeneralDtoResponse (String message, String errorReference, T data) {
        this.error = message;
        this.errorReference = errorReference;
        this.data = data;
    }

    public boolean isValid() {
        return (!StringUtil.isNullOrBlank(error)) || isSuccessResponse();
    }

    public boolean isSuccessResponse() {
        return StringUtil.isNullOrBlank(error) && data != null;
    }
}
