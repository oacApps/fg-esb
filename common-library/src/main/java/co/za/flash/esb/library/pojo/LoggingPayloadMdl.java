package co.za.flash.esb.library.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoggingPayloadMdl {
    private Object requestInPayload;
    private String requestInDateTime;
    private Object requestOutPayload;
    private String requestOutDateTime;
    private Object responseInPayload;
    private String responseInDateTime;
    private Object responseOutPayload;
    private String responseOutDateTime;
    private String apiKey;
    private String keyType;
    private String organization;
    private String endUser;
    private String apiResource;
}




