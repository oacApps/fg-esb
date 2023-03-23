package co.za.flash.esb.library.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DBLoggingPayload implements Serializable {
    private Long id;
    private String organization;
    private String apiKey;
    private String apiName;
    private String apiOperation;
    private String backendBaseUrl;
    private String backendOperation;
    private int httpResponseCode;
    private LocalDateTime requestReceivedDatetime;
    private LocalDateTime responseSentDatetime;
    private Long DurationMs;
    private String correlationId;
    private String requestBody;
    private String responseBody;
}
