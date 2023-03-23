package co.za.flash.esb.avtrouting.service;

import co.za.flash.esb.avtrouting.model.dto.ErrorTmp;
import co.za.flash.esb.avtrouting.model.dto.request.RedeemRequestDTO;
import co.za.flash.esb.avtrouting.model.dto.response.RedeemResponseDTO;
import co.za.flash.esb.avtrouting.mapper.RedeemRequestMapper;
import co.za.flash.esb.avtrouting.mapper.RedeemResponseMapper;
import co.za.flash.esb.avtrouting.model.request.RedeemRequestMdl;
import co.za.flash.esb.avtrouting.model.response.RedeemResponseMdl;
import co.za.flash.esb.avtrouting.restservice.ConsumeWebService;
import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
public class RedeemService {

    @Value("${flash.backend.avt.endpoint.redeem.prod}")
    private String redeemAvtProdEndpoint;

    @Value("${flash.backend.openapi.endpoint.redeem.prod}")
    private String redeemOpenApiProdEndpoint;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    ConsumeWebService webService = new ConsumeWebService();

    LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
    public Object redeem(Map<String, String> headers, RedeemRequestDTO redeemRequest) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);

        loggingPayloadMdl.setRequestInPayload(redeemRequest);
        loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        // 6105843894 (hwb) 6106201852 (betway)
        // AVT QA TEST account : 6333-4205-4889-9948
        // AVT PROD TEST account : 6100000950-001
        if ("6105843894".equals(redeemRequest.getAccountNumber())
                || "6106201852".equals(redeemRequest.getAccountNumber())
                || "6100000950-001".equals(redeemRequest.getAccountNumber())
                || "6333-4205-4889-9948".equals(redeemRequest.getAccountNumber())
        ) {
        RedeemRequestMdl redeemRequestMdl = RedeemRequestMapper.INSTANCE.toMdl(redeemRequest);
        HttpEntity<RedeemRequestMdl> entity = new HttpEntity<RedeemRequestMdl>(redeemRequestMdl, httpHeaders);

        loggingPayloadMdl.setRequestOutPayload(redeemRequestMdl);
        loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        RedeemResponseMdl redeemResponseMdl = webService.redeemService(entity, redeemAvtProdEndpoint);

        loggingPayloadMdl.setResponseInPayload(redeemResponseMdl);
        loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

        RedeemResponseDTO redeemResponseDTO = RedeemResponseMapper.INSTANCE.toDTO(redeemResponseMdl);

        if (redeemResponseMdl.getResponseCode() == 0) {

            redeemResponseDTO.getAmountAuthorised().setCurrency("ZAR");
            redeemResponseDTO.getAvailableBalance().setCurrency("ZAR");
            redeemResponseDTO.getLedgerBalance().setCurrency("ZAR");

            StringBuilder receipt = new StringBuilder();
            redeemResponseDTO.setActionCode("0000");
            String createdDateStr = redeemResponseMdl.getData().getTransaction().getCreated();

            Date date = null;
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            utcFormat.setTimeZone(TimeZone.getTimeZone("Africa/Johannesburg"));
            try {
                date = utcFormat.parse(createdDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            LocalDateTime createdDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

            Map<String, String> accounts = new HashMap();
            accounts.put("6105843894", "HOLLYWOOD B");
            accounts.put("6106201852", "RAGING RIVE");
            accounts.put("6100000950-001", "WAZAA-001");
            accounts.put("6333-4205-4889-9948", "AVT-QA-TEST");

            String createdMonth = String.valueOf(createdDate.getMonth()).substring(0,3).toLowerCase();
            receipt.append(createdDate.getDayOfMonth())
                    .append(" ")
                    .append(createdMonth.substring(0,1).toUpperCase() + createdMonth.substring(1))
                    .append(" ")
                    .append(createdDate.getHour()).append(":").append(createdDate.getMinute())
                    .append(";Ref: ").append(redeemResponseMdl.getData().getSwitchId())
                    .append(";Shop: ").append(accounts.get(redeemRequest.getAccountNumber()))
                    .append(";Transaction Successful;1FORYOU PIN Redeemed;Amount: R")
                    .append(redeemResponseMdl.getData().getAmountCents() / 100).append(".00")
                    .append(";www.1foryou.com;");
            redeemResponseDTO.setReceipt(receipt.toString());
        } else {
            ErrorTmp error = getError(redeemResponseMdl.getResponseCode());
            redeemResponseDTO.setActionCode(error.getActionCode());
            redeemResponseDTO.setErrorMessage(error.getErrorMessage());

            redeemResponseDTO.setAmountAuthorised(null);
            redeemResponseDTO.setAvailableBalance(null);
            redeemResponseDTO.setLedgerBalance(null);
            redeemResponseDTO.setReceipt(error.getErrorMessage());
            redeemResponseDTO.setTransactionReference("0");
        }

        loggingPayloadMdl.setResponseOutPayload(redeemResponseDTO);
        loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));

        return redeemResponseDTO;

    }
        HttpEntity<RedeemRequestDTO> entity = new HttpEntity<RedeemRequestDTO>(redeemRequest,httpHeaders);
        return webService.openAPIBackEnd(entity, redeemOpenApiProdEndpoint);
    }

    private ErrorTmp getError(int code) {

        ErrorTmp responseError = new ErrorTmp();
        /*switch(code){
            case 2003: responseError.setActionCode("1016"); responseError.setErrorMessage("Insufficient funds."); break;
            case 2222:
            case 2400: responseError.setActionCode("9999"); responseError.setErrorMessage("System error."); break;
            case 2202: responseError.setActionCode("1011"); responseError.setErrorMessage("Account not found."); break;
            case 2401: responseError.setActionCode("1027"); responseError.setErrorMessage("Voucher used."); break;
            case 2402: responseError.setActionCode("1821"); responseError.setErrorMessage("Voucher not found."); break;
            case 2403: responseError.setActionCode("1823"); responseError.setErrorMessage("Voucher cancelled."); break;
            case 2404: responseError.setActionCode("1009"); responseError.setErrorMessage("Invalid acquirer."); break;
            case 2001: responseError.setActionCode("9111"); responseError.setErrorMessage("System temporarily unavailable, please try again in a couple of minutes. If the problem persists please contact Flash Support."); break;
            case 175: responseError.setActionCode("1821"); responseError.setErrorMessage("Invalid One Voucher PIN provided."); break;
            case 151: responseError.setActionCode("1824"); responseError.setErrorMessage("Voucher used."); break;
            default: responseError.setActionCode("9999"); responseError.setErrorMessage("Unable to redeem OneVoucher!"); break;
        }*/
        if(code == 2402){
            responseError.setActionCode("1821");
            responseError.setErrorMessage("Voucher not found.");
        } else if(code == 2401){
            responseError.setActionCode("1824");
            responseError.setErrorMessage("Voucher Used.");
        } else if(code == 2400){
            responseError.setActionCode("1823");
            responseError.setErrorMessage("Voucher Cancelled.");
        } else {
            responseError.setActionCode("9999");
            responseError.setErrorMessage("Unable to redeem OneVoucher!");
       }
        return responseError;
    }

}
