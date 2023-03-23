package co.za.flash.esb.giftvoucher.service;

import co.za.flash.esb.giftvoucher.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.giftvoucher.dto.response.FaultResponse;
import co.za.flash.esb.giftvoucher.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.giftvoucher.mapper.PurchaseRequestMapper;
import co.za.flash.esb.giftvoucher.mapper.PurchaseResponseMapper;
import co.za.flash.esb.giftvoucher.model.PurchaseRequestMdl;
import co.za.flash.esb.giftvoucher.model.PurchaseResponseMdl;
import co.za.flash.esb.giftvoucher.restservice.ConsumeWebService;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class GiftVoucherService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConsumeWebService webService;

    LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

    //taking DTOs and converting into the model and adding headers
    public PurchaseResponseDTO purchaseRequestService(Map<String, String> headers, PurchaseRequestDTO purchaserequestDTO) throws JSONException, JWTException {

        JwtExtractor jwtExtractor;

        loggingPayloadMdl.setApiKey("GiftVoucher-purchase");
        loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
        loggingPayloadMdl.setRequestInPayload(purchaserequestDTO);

        PurchaseResponseDTO purchaseResponseDTO = new PurchaseResponseDTO();

        String jwtToken = (String)headers.get(this.wso2JwtKeys.xJwtAssertion);
        if(null == jwtToken) {
            purchaseResponseDTO.setResponseMessage("Bad Request: JWT Token missing from header");
            purchaseResponseDTO.setResponseCode(400);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            return purchaseResponseDTO;
        }
        try {
            jwtExtractor = new JwtExtractor();
            jwtExtractor.extract(headers);

            if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                purchaseResponseDTO.setResponseCode(900910);
                purchaseResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");

                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));

                return purchaseResponseDTO;
            }
            loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());

            if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                purchaseResponseDTO.setResponseCode(900910);
                purchaseResponseDTO.setResponseMessage("Can not access the required resource with the provided access token. KeyType missing.");

                loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
                loggingPayloadMdl.setRequestOutPayload(purchaseResponseDTO);

                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. KeyType missing.");
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));

                return purchaseResponseDTO;
            }

            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

        }catch (Exception e){
            LOGGER.error("JwtExtractor: " + e.getMessage());
            purchaseResponseDTO.setResponseMessage("Bad Request: Invalid JWT Token");
            purchaseResponseDTO.setResponseCode(400);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            return purchaseResponseDTO;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        JSONArray roles = jwtExtractor.getRole();
        boolean allowedToTransact = false;
        if(roles.length() > 0){
            String roleName = voucherPermission(purchaserequestDTO.getVoucherType());
            for (int i = 0; i < roles.length(); i++) {
                if(roleName.equals(roles.getString(i))) {
                    allowedToTransact = true;
                    break;
                }
            }
        }

        allowedToTransact = true;

        if(!allowedToTransact) {
            FaultResponse faultResponse = new FaultResponse();
            faultResponse.setType("Status report");
            faultResponse.setMessage("Invalid Credentials");
            faultResponse.setDescription("You do not have access for this selection. Please contact your Integration Specialist for more information.");

            purchaseResponseDTO.setResponseCode(-1);
            purchaseResponseDTO.setFault(faultResponse);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
            LOGGER.error("The access token does not allow to to purchase the requested product: " + purchaserequestDTO.getVoucherType());
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            return purchaseResponseDTO;
        }

        try {
            PurchaseRequestMdl purchaseRequestMdl = PurchaseRequestMapper.INSTANCE.toMdl(purchaserequestDTO);
            HttpEntity<PurchaseRequestMdl> entity = new HttpEntity<PurchaseRequestMdl>(purchaseRequestMdl, httpHeaders);

            loggingPayloadMdl.setRequestOutPayload(purchaseRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            PurchaseResponseMdl purchaseResponseMdl = webService.purchaseRequestService(entity, jwtExtractor.getKeyType(), loggingPayloadMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setResponseInPayload(purchaseResponseMdl);

            purchaseResponseDTO = PurchaseResponseMapper.INSTANCE.toDTO(purchaseResponseMdl);
            purchaseResponseDTO.setAcquirerReference(purchaserequestDTO.getAcquirerReference());
            purchaseResponseDTO.setSequenceNumber(purchaserequestDTO.getSequenceNumber());
            purchaseResponseDTO.setUserId(purchaserequestDTO.getUserId());
            purchaseResponseDTO.setResponseMessage(purchaseResponseDTO.getResponseMessage().replace("General_", ""));

            if(null != purchaseResponseDTO.getResponseTime()) {
                purchaseResponseDTO.setResponseTime(toDateFormat(purchaseResponseDTO.getResponseTime().substring(0, 19).replace("T", " "), 0));
            }else {
                String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                purchaseResponseDTO.setResponseTime(timeStamp);
            }
            if(null == purchaseResponseDTO.getVoucher().getPin()){
                purchaseResponseDTO.setVoucher(null);
            }else {
               purchaseResponseDTO.getVoucher().setExpiryDate(purchaseResponseDTO.getVoucher().getExpiryDate());
            }

            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        } finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        return purchaseResponseDTO;
    }

    // to 0 = dd/MM/yyyy HH:mm:ss; 1= dd-MM-yyyy HH:mm:ss
    private String toDateFormat(String dateTime, int to){

        String strDate ="";
        SimpleDateFormat formatterFrom=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatterTo= to == 0 ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") : new SimpleDateFormat("dd-MM-yyyyTHH:mm:ss");
        try {
            Date date=formatterFrom.parse(dateTime);
            strDate = formatterTo.format(date);
        } catch (ParseException e) {
            LOGGER.error("date ParseException: " + e.getLocalizedMessage());
        }

        return strDate;
    }

    private String voucherPermission(String voucherName){
        String voucherPerm="";
        switch (voucherName){
            case "NETFLIX":
                voucherPerm="GiftVoucher-NETFLIX-perm"; break;
            case "SPOTIFY":
                voucherPerm="GiftVoucher-SPOTIFY-perm"; break;
            case "UBER":
                voucherPerm="GiftVoucher-UBER-perm"; break;
            case "1VOUCHER":
            case "1FORYOU":
                voucherPerm="GiftVoucher-1FORYOU-perm"; break;
            case "ROBLOX":
                voucherPerm="GiftVoucher-ROBLOX-perm"; break;
            default:
                voucherPerm="";
        }
        return voucherPerm;
    }
}









