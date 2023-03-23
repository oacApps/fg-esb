package co.za.flash.esb.oneforyou.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.oneforyou.dto.response.RedeemResponseTraderDTO;
import co.za.flash.esb.oneforyou.mapper.PurchaseRequestMapper;
import co.za.flash.esb.oneforyou.mapper.PurchaseResponseMapper;
import co.za.flash.esb.oneforyou.mapper.RedeemRequestMapper;
import co.za.flash.esb.oneforyou.mapper.RedeemResponseMapper;
import co.za.flash.esb.oneforyou.model.SequenceMdl;
import co.za.flash.esb.oneforyou.model.request.trader.AcquirerTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.PurchaseRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.RedeemRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.PurchaseResponseTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.RedeemResponseTraderMdl;
import co.za.flash.esb.oneforyou.restservice.ConsumeWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class OneForUServiceImplTrader implements OneForUServiceInf {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //@Autowired
    ConsumeWebService webService = new ConsumeWebService();

    @Override
    public PurchaseResponseDTO purchaseService(HttpHeaders headers,
                                               PurchaseRequestDTO purchaseRequestDTO,
                                               String organization,
                                               String keyType,
                                               String backEndUrl,
                                               String seqUrl) {

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        PurchaseResponseDTO purchaseResponseDTO = new PurchaseResponseDTO();
        try {

            loggingPayloadMdl.setApiKey("1forYou-trader-purchase");
            loggingPayloadMdl.setOrganization(organization);
            loggingPayloadMdl.setKeyType(keyType);
            loggingPayloadMdl.setApiResource(backEndUrl);

            // What we receive from API manager
            loggingPayloadMdl.setRequestInPayload(purchaseRequestDTO);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            Long seqNum = getSequenceNumber(headers, seqUrl, purchaseRequestDTO.getAcquirer().getAccount().getAccountNumber(), purchaseRequestDTO.getRequestId(), "1forYou-trader-purchase");

            PurchaseRequestTraderMdl purchaseRequestTraderMdl = PurchaseRequestMapper.INSTANCE.toTTMdl(purchaseRequestDTO);

            List<AcquirerTraderMdl> acquirerReference= new ArrayList<>();
            AcquirerTraderMdl acquirerTraderMdl = new AcquirerTraderMdl();
            acquirerTraderMdl.setKey("clientReference");

            if(null != purchaseRequestDTO.getAcquirer().getEntityTag()) {
                acquirerTraderMdl.setValue(purchaseRequestDTO.getAcquirer().getEntityTag());
            }else {
                acquirerTraderMdl.setValue(purchaseRequestDTO.getRequestId());
            }

            acquirerReference.add(acquirerTraderMdl);
            purchaseRequestTraderMdl.setAcquirerReference(acquirerReference);
            purchaseRequestTraderMdl.setSequenceNumber(seqNum);
            // What we Send to Back end
//            PurchaseTraderRequestDTO purchaseTraderRequestDTO = PurchaseTraderMapper.toPurchaseTraderRequestDTO(purchaseRequestDTO, seqNumber);

            loggingPayloadMdl.setRequestOutPayload(purchaseRequestTraderMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            HttpEntity<PurchaseRequestTraderMdl> purchaseRequestEntity = new HttpEntity<PurchaseRequestTraderMdl>(purchaseRequestTraderMdl, headers);

            PurchaseResponseTraderMdl purchaseResponseTraderTreasuryMdl = webService.oneForYouTraderPurchase(purchaseRequestEntity, backEndUrl);

            purchaseResponseDTO = PurchaseResponseMapper.INSTANCE.toDTOFromTrader(purchaseResponseTraderTreasuryMdl);

            purchaseResponseDTO.setRequestId(purchaseRequestDTO.getRequestId());
            purchaseResponseDTO.getAcquirer().getAccount().setAccountNumber(purchaseRequestDTO.getAcquirer().getAccount().getAccountNumber());
            purchaseResponseDTO.getAcquirer().setEntityTag(purchaseRequestDTO.getAcquirer().getEntityTag());

            if(null != purchaseResponseDTO.getVoucher().getExpiryDate()){
                purchaseResponseDTO.getVoucher().setExpiryDate(toDateFormat(purchaseResponseDTO.getVoucher().getExpiryDate()));
            }else{
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                purchaseResponseDTO.getVoucher().setExpiryDate(timeStamp);
            }

            // What we Receive from Back end
            loggingPayloadMdl.setResponseInPayload(purchaseResponseTraderTreasuryMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            // What we send to API Manager
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setApiResource(backEndUrl);;
        } catch (Exception e) {
            LOGGER.info("Trader purchaseService Exception payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.error("Trader purchaseService Exception: ");
            e.printStackTrace();
        } finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return purchaseResponseDTO;
    }

    @Override
    public RedeemResponseTraderDTO redeemService(HttpHeaders httpHeaders,
                                                 RedeemRequestDTO redeemRequestDTO,
                                                 String organization,
                                                 String keyType,
                                                 String backEndUrl,
                                                 String seqUrl) {
        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        RedeemResponseTraderDTO redeemResponseDTO = new RedeemResponseTraderDTO();
        try {

            loggingPayloadMdl.setApiKey("1forYou-trader-redeem");
            loggingPayloadMdl.setOrganization(organization);
            loggingPayloadMdl.setKeyType(keyType);
            loggingPayloadMdl.setApiResource(backEndUrl);

            // What we receive from API manager
            loggingPayloadMdl.setRequestInPayload(redeemRequestDTO);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            Long seqNum = getSequenceNumber(httpHeaders, seqUrl, redeemRequestDTO.getAcquirer().getAccount().getAccountNumber(), redeemRequestDTO.getRequestId(), "1forYou-trader-redeem");

            RedeemRequestTraderMdl redeemRequestTraderMdl = RedeemRequestMapper.INSTANCE.toTTMdl(redeemRequestDTO);
           /* if(null != redeemRequestDTO.getCustomerContact()){
                redeemRequestTraderMdl.setCustomerContact(redeemRequestDTO.getCustomerContact());
            }*/

            if(null != redeemRequestDTO.getRequestId()){
                redeemRequestTraderMdl.setReferenceId(redeemRequestDTO.getRequestId());
            }

            List<AcquirerTraderMdl> acquirerReference= new ArrayList<>();
            AcquirerTraderMdl acquirerTraderMdl = new AcquirerTraderMdl();
            acquirerTraderMdl.setKey("clientReference");

            if(null != redeemRequestDTO.getAcquirer().getEntityTag()) {
                acquirerTraderMdl.setValue(redeemRequestDTO.getAcquirer().getEntityTag());
            }else {
                acquirerTraderMdl.setValue(redeemRequestDTO.getRequestId());
            }
            acquirerReference.add(acquirerTraderMdl);
            redeemRequestTraderMdl.setAcquirerReference(acquirerReference);
            redeemRequestTraderMdl.setSequenceNumber(seqNum);

            loggingPayloadMdl.setRequestOutPayload(redeemRequestTraderMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            HttpEntity<RedeemRequestTraderMdl> entity = new HttpEntity<RedeemRequestTraderMdl>(redeemRequestTraderMdl,httpHeaders);
            RedeemResponseTraderMdl redeemResponseTraderMdl = webService.oneForYouTraderRedeem(entity, backEndUrl);

            try {
                redeemResponseDTO = RedeemResponseMapper.INSTANCE.toDTOFromTrader(redeemResponseTraderMdl);
            }catch (Exception e){
                LOGGER.info("Backend response received redeemResponseTraderMdl: " + JsonStringMapper.toJsonString(redeemResponseTraderMdl));
                LOGGER.error("RuntimeException redeemResponseTraderMdl to redeemResponseDTO: " + e.getLocalizedMessage());
                e.printStackTrace();
                redeemResponseDTO.setResponseCode(Integer.valueOf(redeemResponseTraderMdl.getResponseCode()));
                redeemResponseDTO.setResponseMessage(redeemResponseTraderMdl.getResponseMessage());
            }

            redeemResponseDTO.setRequestId(redeemRequestDTO.getRequestId());
            if(null != redeemResponseDTO.getAcquirer()) {
                if(null != redeemResponseDTO.getAcquirer().getAccount()) {
                    redeemResponseDTO.getAcquirer().getAccount().setAccountNumber(Long.valueOf(redeemRequestDTO.getAcquirer().getAccount().getAccountNumber()));
                }
                redeemResponseDTO.getAcquirer().setEntityTag(redeemRequestDTO.getAcquirer().getEntityTag());
            }

            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            // What we Receive from Back end
            loggingPayloadMdl.setResponseInPayload(redeemResponseTraderMdl);

           // What we send to API Manager
            loggingPayloadMdl.setResponseOutPayload(redeemResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
            loggingPayloadMdl.setApiResource(backEndUrl);;

        } catch (Exception e) {
            LOGGER.info("Trader redeemService Exception payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            LOGGER.error("Trader redeemService Exception: ");
            e.printStackTrace();
        } finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return redeemResponseDTO;
    }


    public Long getSequenceNumber(HttpHeaders headers, String seqUrl, String accountNumber, String sequenceNumber, String messageType){
        Long sn = null;
        try{
            SequenceMdl sequenceMdl=new SequenceMdl();
            sequenceMdl.setAccountNumber(accountNumber);
            sequenceMdl.setSequenceNumber(sequenceNumber);
            sequenceMdl.setMessageType(messageType);

            HttpEntity<SequenceMdl> purchaseRequestEntity = new HttpEntity<SequenceMdl>(sequenceMdl, headers);
            sn = webService.oneForYouTraderSequenceNumber(purchaseRequestEntity, seqUrl);
        }   catch (Exception e){
            LOGGER.error("Trader getSequenceNumber Exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return sn;
    }

    private String toDateFormat(String dateTime){

        String strDate ="";
        SimpleDateFormat formatterFrom=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatterTo= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=formatterFrom.parse(dateTime);
            strDate = formatterTo.format(date);
        } catch (ParseException e) {
            LOGGER.error("date ParseException: " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return strDate;
    }
}
