package co.za.flash.credential.management.dto.response;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import co.za.flash.credential.management.helper.utils.JsonParseUtil;
import co.za.flash.credential.management.helper.utils.SoapEnvelopeParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Data;
import org.w3c.dom.Node;

@Data
public class ErrorDto {
    private String status;
    private String schemas;
    private String scimType;
    private String detail;

    public ErrorDto(String statusCode, String message) {
        this.status = statusCode;
        this.detail = message;
    }

    private ErrorDto(String status,
     String schemas,
     String scimType,
     String detail) {
        this.status = status;
        this.schemas = schemas;
        this.scimType = scimType;
        this.detail = detail;
    }

    public static ErrorDto parseJson(String rawResponse) {
        try {
            // manually parse the response. In case the fields are optional
            JsonObject rootObj = JsonParser.parseString(rawResponse).getAsJsonObject();
            String status = JsonParseUtil.parseString(rootObj, "status", "");
            String schemas = JsonParseUtil.parseString(rootObj, "schemas", "");
            String scimType = JsonParseUtil.parseString(rootObj, "scimType", "");
            String detail = JsonParseUtil.parseString(rootObj, "detail", "");
            return new ErrorDto(status, schemas, scimType, detail);
        } catch (JsonParseException je) {
            return null;
        }
    }

    public static ErrorDto parseXml(String rawResponse) {
        String status = "";
        String details = "";
        Node errorNode = SoapEnvelopeParser.getNodeFromBody(rawResponse, SoapEnvFieldNames.Error.fieldName);
        if (errorNode != null) {
            status = SoapEnvelopeParser.parseString(errorNode, SoapEnvFieldNames.ErrorCode.fieldName, "");
            details = SoapEnvelopeParser.parseString(errorNode, SoapEnvFieldNames.ErrorString.fieldName, "");
        }
        //<soapenv:Fault><faultcode>soapenv:Server</faultcode><faultstring>UserAlreadyExisting:Username already exists in the system. Please pick another username.</faultstring><detail/></soapenv:Fault>
        return new ErrorDto(status, "", "", details);
    }
}
