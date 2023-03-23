package co.za.flash.esb.oneforyou.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.oneforyou.dto.response.RedeemResponseDTO;
import co.za.flash.esb.oneforyou.dto.shared.ChangeVoucherDTO;
import co.za.flash.esb.oneforyou.mapper.PurchaseRequestMapper;
import co.za.flash.esb.oneforyou.mapper.PurchaseResponseMapper;
import co.za.flash.esb.oneforyou.mapper.RedeemRequestMapper;
import co.za.flash.esb.oneforyou.mapper.RedeemResponseMapper;
import co.za.flash.esb.oneforyou.mapper.shared.ChangeVoucherMapper;
import co.za.flash.esb.oneforyou.model.request.PurchaseRequestMdl;
import co.za.flash.esb.oneforyou.model.request.RedeemRequestMdl;
import co.za.flash.esb.oneforyou.model.response.PurchaseResponseMdl;
import co.za.flash.esb.oneforyou.model.response.RedeemResponseMdl;
import co.za.flash.esb.oneforyou.model.shared.AcquirerReferenceMdl;
import co.za.flash.esb.oneforyou.restservice.ConsumeWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OneForUServiceImplAvt implements OneForUServiceInf {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /*@Autowired*/
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
            // What we receive from API manager
            loggingPayloadMdl.setApiKey("1forYou-avt-purchase");
            loggingPayloadMdl.setOrganization(organization);
            loggingPayloadMdl.setKeyType(keyType);
            loggingPayloadMdl.setApiResource(backEndUrl);

            loggingPayloadMdl.setRequestInPayload(purchaseRequestDTO);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
            // organization Name is user name for JWT
            PurchaseRequestMdl purchaseRequestMdl = setPurchaseRequestMdl(purchaseRequestDTO, organization);
            // What we Send to Back end
            HttpEntity<PurchaseRequestMdl> entity = new HttpEntity<PurchaseRequestMdl>(purchaseRequestMdl, headers);

            loggingPayloadMdl.setRequestOutPayload(purchaseRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            PurchaseResponseMdl purchaseResponseMdl = webService.avtPurchaseService(entity, backEndUrl);

            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());
            // What we Receive from Back end
            loggingPayloadMdl.setResponseInPayload(purchaseResponseMdl);

            if(purchaseResponseMdl.getData() !=null) {
                purchaseResponseMdl.getData().getToken().setExpiryDate(
                        FlashDates.stringDateTimeToStringDateTime(purchaseResponseMdl.getData().getToken().getExpiryDate())
                );
            }

            purchaseResponseDTO = PurchaseResponseMapper.INSTANCE.toDTO(purchaseResponseMdl);

            if (purchaseResponseMdl.getResponseCode() == 0) {
                purchaseResponseDTO.setAcquirer(purchaseRequestDTO.getAcquirer());
            }
            // What we send to API Manager
            loggingPayloadMdl.setResponseOutPayload(purchaseResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        } catch (Exception e) {
            LOGGER.error("AVT purchaseService Exception: " + e.getLocalizedMessage());
            LOGGER.info("AVT purchaseService Exception payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            e.printStackTrace();
        } finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return purchaseResponseDTO;
    }

    @Override
    public RedeemResponseDTO redeemService(HttpHeaders httpHeaders,
                                           RedeemRequestDTO redeemRequestDTO,
                                           String organization,
                                           String keyType,
                                           String backEndUrl,
                                           String seqUrl){
        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        RedeemResponseDTO redeemResponseDTO = new RedeemResponseDTO();

        try {

            loggingPayloadMdl.setApiKey("1forYou-avt-redeem");
            loggingPayloadMdl.setOrganization(organization);
            loggingPayloadMdl.setKeyType(keyType);
            loggingPayloadMdl.setApiResource(backEndUrl);

            // What we receive from API manager
            loggingPayloadMdl.setRequestInPayload(redeemRequestDTO);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            AcquirerReferenceMdl acquirerReferenceMdl= new AcquirerReferenceMdl();
            acquirerReferenceMdl.setKey("dd");
            acquirerReferenceMdl.setValue("dd");
            List<AcquirerReferenceMdl> acquirerReferenceMdlList = new ArrayList<>();
            acquirerReferenceMdlList.add(acquirerReferenceMdl);

            RedeemRequestMdl redeemRequestMdl = RedeemRequestMapper.INSTANCE.toMdl(redeemRequestDTO);
            if(null != redeemRequestDTO.getCustomerContact()) {
                if("SMS".equals(redeemRequestDTO.getCustomerContact().mechanism)){
                    redeemRequestMdl.setMsisdn(redeemRequestDTO.getCustomerContact().getAddress());
                }
            }
            if(null == redeemRequestMdl.getUserId() || redeemRequestMdl.getUserId().isEmpty()){
                redeemRequestMdl.setUserId(organization);
            }
            redeemRequestMdl.setAcquirerReference(acquirerReferenceMdlList);

            // What we Send to Back end
            loggingPayloadMdl.setRequestOutPayload(redeemRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            HttpEntity<RedeemRequestMdl> entity = new HttpEntity<RedeemRequestMdl>(redeemRequestMdl,httpHeaders);

            RedeemResponseMdl redeemResponseMdl = webService.redeemService(entity, backEndUrl);

            // What we Receive from Back end
            loggingPayloadMdl.setResponseInPayload(redeemResponseMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            redeemResponseDTO = RedeemResponseMapper.INSTANCE.toDTO(redeemResponseMdl);
            if(null != redeemResponseMdl.getData()) {
                ChangeVoucherDTO changeVoucherDTO = ChangeVoucherMapper.INSTANCE.toDTO(redeemResponseMdl.getData().getChangeVoucher());
                redeemResponseDTO.setChangeVoucher(changeVoucherDTO);
            }
            redeemResponseDTO.setAcquirer(redeemRequestDTO.getAcquirer());

            // What we send to API Manager
            loggingPayloadMdl.setResponseOutPayload(redeemResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

        } catch (Exception e) {
            LOGGER.error("AVT redeemService Exception: " + e.getLocalizedMessage());
            LOGGER.info("AVT redeemService Exception payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            e.printStackTrace();
        } finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return redeemResponseDTO;
    }
    private PurchaseRequestMdl setPurchaseRequestMdl(PurchaseRequestDTO purchaseRequestDTO, String userID) {
        PurchaseRequestMdl purchaseRequestMdl = new PurchaseRequestMdl();
        try{
        purchaseRequestMdl = PurchaseRequestMapper.INSTANCE.toMdl(purchaseRequestDTO);
        purchaseRequestMdl.setUserId(userID);
        Map<String, String>  acquirerReferenceMdl = new HashMap<>();
        acquirerReferenceMdl.put("clientReference", purchaseRequestDTO.getAcquirer().getEntityTag());
        List acquirerReferenceMdlList = new ArrayList();
        acquirerReferenceMdlList.add(acquirerReferenceMdl);

        purchaseRequestMdl.setAcquirerReference(acquirerReferenceMdlList);
        } catch (RuntimeException e){
            LOGGER.error("AVT setPurchaseRequestMdl Exception: " + e.getLocalizedMessage());
            LOGGER.info("AVT setPurchaseRequestMdl Exception payload: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
        }

        return purchaseRequestMdl;
    }
}
