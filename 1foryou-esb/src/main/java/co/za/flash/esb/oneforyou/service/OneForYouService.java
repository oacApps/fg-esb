package co.za.flash.esb.oneforyou.service;

import co.za.flash.esb.library.FlashDates;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.enums.EnvironmentEnum;
import co.za.flash.esb.library.jwt.JWTException;
import co.za.flash.esb.library.jwt.JwtExtractor;
import co.za.flash.esb.library.jwt.Wso2JwtKeys;
import co.za.flash.esb.library.pojo.AccountsData;
import co.za.flash.esb.library.pojo.DBLoggingPayload;
import co.za.flash.esb.library.pojo.LoggingPayloadMdl;
import co.za.flash.esb.oneforyou.dto.request.*;
import co.za.flash.esb.oneforyou.dto.response.*;
import co.za.flash.esb.oneforyou.dto.shared.AccountDTO;
import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.exceptions.NotFoundException;
import co.za.flash.esb.oneforyou.mapper.RefundRedemptionMapper;
import co.za.flash.esb.oneforyou.mapper.RefundRequestMapper;
import co.za.flash.esb.oneforyou.mapper.RefundResponseMapper;
import co.za.flash.esb.oneforyou.mapper.ReverseRedemptionMapper;
import co.za.flash.esb.oneforyou.model.SequenceMdl;
import co.za.flash.esb.oneforyou.model.request.RefundRequestMdl;
import co.za.flash.esb.oneforyou.model.request.trader.AcquirerTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.RefundRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.ReverseRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.response.RefundResponseMdl;
import co.za.flash.esb.oneforyou.model.response.trader.RefundRedemptionResponseTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.ReversalsRedemptionResponseTraderMdl;
import co.za.flash.esb.oneforyou.restservice.ConsumeWebService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OneForYouService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ConsumeWebService webService;

    @Value("${flash.accounts.data.json.file}")
    private String accountsDataFile;

    @Value("${flash.avt.test.redeem.accounts.data.json.file}")
    private String avtRedeemTestAccountsDataFile;

    @Value("${flash.avt.test.purchase.accounts.data.json.file}")
    private String avtPurchaseTestAccountsDataFile;

    List<AccountsData> accountsData = new ArrayList<>();
    List<AccountsData> avtTestAccountsData = new ArrayList<>();
    List<AccountsData> avtPurchaseTestAccountsData = new ArrayList<>();

    private boolean isAccountExists;

    @Value("${flash.aggregation.backend.endpoint.redeem.prod}")
    private String redeemProdAvtEndpoint;
    @Value("${flash.aggregation.backend.endpoint.redeem.sandbox}")
    private String redeemSandboxAvtEndpoint;

    @Value("${flash.aggregation.backend.endpoint.refund.prod}")
    private String refundProdAvtEndpoint;
    @Value("${flash.aggregation.backend.endpoint.refund.sandbox}")
    private String refundSandboxAvtEndpoint;

    @Value("${flash.aggregation.backend.endpoint.purchase.prod}")
    private String purchaseProdAvtEndpoint;
    @Value("${flash.aggregation.backend.endpoint.purchase.sandbox}")
    private String purchaseSandboxAvtEndpoint;

    @Value("${flash.oneforyou.trader.esb.prod}")
    private String oneForYouTraderEsbProd;
    @Value("${flash.oneforyou.trader.esb.sandbox}")
    private String oneForYouTraderEsbSandbox;

    Wso2JwtKeys wso2JwtKeys = new Wso2JwtKeys();

    private void init() {
        if (this.accountsData.isEmpty()) {
            loadAccountsData();
        }
    }


    //taking DTOs and converting into the model and adding headers
    public PurchaseResponseDTO purchaseService(Map<String, String> headers, PurchaseRequestDTO purchaseRequestDTO) {
        init();

        JwtExtractor jwtExtractor = new JwtExtractor();

        PurchaseResponseDTO purchaseResponseDTO = new PurchaseResponseDTO();
        String backEndUrl = "";
        try{
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                try {
                    jwtExtractor.extract(headers);
                } catch (JWTException | JSONException e) {
                    LOGGER.error("Purchase jwtExtractor JWTException: " + e.getLocalizedMessage());
                    LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
                    e.printStackTrace();
                } catch (Exception e) {
                    LOGGER.error("Purchase jwtExtractor exception: " + e.getLocalizedMessage());
                    LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
                    e.printStackTrace();
                }

                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    PurchaseResponseDTO purchaseResponse = new PurchaseResponseDTO();
                    purchaseResponse.setResponseCode(900910);
                    purchaseResponse.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
                    return purchaseResponse;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    PurchaseResponseDTO purchaseResponse = new PurchaseResponseDTO();
                    purchaseResponse.setResponseCode(900910);
                    purchaseResponse.setResponseMessage("Can not access the required resource with the provided access token. Key type missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key type missing.");
                    LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
                    return purchaseResponse;
                }
            }else {
                purchaseResponseDTO.setResponseCode(900401);
                purchaseResponseDTO.setResponseMessage("Invalid credential");
                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
                return purchaseResponseDTO;
            }


            OneForUServiceInf oneForUService = initOneForUService(purchaseRequestDTO.getAcquirer());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);

            String seqUrl = jwtExtractor.getKeyType();

            if(oneForUService instanceof  OneForUServiceImplTrader){
                String urlRoot= EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(jwtExtractor.getKeyType()) ? oneForYouTraderEsbProd : oneForYouTraderEsbSandbox;
                backEndUrl = urlRoot + "/purchase";
                seqUrl =  urlRoot + "/sequenceNumber";
            }else {
                backEndUrl = EnvironmentEnum.PRODUCTION.getValue().equalsIgnoreCase(jwtExtractor.getKeyType()) ? purchaseProdAvtEndpoint : purchaseSandboxAvtEndpoint;
            }

            purchaseResponseDTO = oneForUService.purchaseService(httpHeaders, purchaseRequestDTO, jwtExtractor.getOrganization(), jwtExtractor.getKeyType(), backEndUrl, seqUrl);

        } catch (Exception e) {
            LOGGER.info("Request in: " + JsonStringMapper.toJsonString(purchaseRequestDTO));
            LOGGER.error("Purchase service exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return purchaseResponseDTO;
    }


    public Object redeemService(Map<String, String> headers, RedeemRequestDTO redeemRequestDTO) {
        init();
        JwtExtractor jwtExtractor = new JwtExtractor();
        Object redeemResponseDTO = new Object();
        String backEndUrl = "";

        try {
            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                try {
                    jwtExtractor.extract(headers);
                } catch (JWTException | JSONException e) {
                    LOGGER.error("Redeem jwtExtractor JWTException: " + e.getLocalizedMessage());
                    LOGGER.info("Redeem Request in: " + JsonStringMapper.toJsonString(redeemRequestDTO));
                    e.printStackTrace();
                } catch (Exception e) {
                    LOGGER.error("Redeem jwtExtractor exception: " + e.getLocalizedMessage());
                    LOGGER.info("Redeem Request in: " + JsonStringMapper.toJsonString(redeemRequestDTO));
                    e.printStackTrace();
                }

                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    RedeemResponseDTO redeemResponse = new RedeemResponseDTO();
                    redeemResponse.setResponseCode(900910);
                    redeemResponse.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.error("Redeem Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    return redeemResponseDTO;
                }
                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    RedeemResponseDTO redeemResponse = new RedeemResponseDTO();
                    redeemResponse.setResponseCode(900910);
                    redeemResponse.setResponseMessage("Can not access the required resource with the provided access token. Key type missing.");
                    LOGGER.error("Redeem Error code (900910): Can not access the required resource with the provided access token. Key type missing.");
                    return redeemResponseDTO;
                }
            }else {
                RedeemResponseDTO redeemResponse = new RedeemResponseDTO();
                redeemResponse.setResponseCode(900401);
                redeemResponse.setResponseMessage("Invalid credential");
                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                LOGGER.info("Request in: " + JsonStringMapper.toJsonString(redeemRequestDTO));
                return redeemResponse;
            }



            OneForUServiceInf oneForUService = initOneForUService(redeemRequestDTO.getAcquirer());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);
            String seqUrl = jwtExtractor.getKeyType();
            if(oneForUService instanceof  OneForUServiceImplTrader){
                String urlRoot = jwtExtractor.getKeyType().equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? oneForYouTraderEsbProd : oneForYouTraderEsbSandbox;
                backEndUrl = urlRoot + "/redeem";
                seqUrl =  urlRoot + "/sequenceNumber";
            }else{
                backEndUrl = jwtExtractor.getKeyType().equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? redeemProdAvtEndpoint : redeemSandboxAvtEndpoint;
            }
            redeemResponseDTO = oneForUService.redeemService(httpHeaders, redeemRequestDTO, jwtExtractor.getOrganization(), jwtExtractor.getKeyType(), backEndUrl, seqUrl);

        } catch (Exception e) {
            LOGGER.info("Redeem Request in: " + JsonStringMapper.toJsonString(redeemRequestDTO));
            LOGGER.error("Redeem service exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return redeemResponseDTO;
    }



    public RefundResponseDTO refundService(Map<String, String> headers, RefundRequestDTO refundRequest){
        init();

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        RefundResponseDTO refundResponseDTO = new RefundResponseDTO();
        try {

            this.isAccountExists = this.accountsData.stream().anyMatch(
                    acData -> acData.getAccountId().equals(refundRequest.getAcquirer().getAccount().getAccountNumber())
            );

            // If Trader Treasury - refund is not allowed
            if (this.isAccountExists) {
                refundResponseDTO.setResponseCode(900950);
                refundResponseDTO.setResponseMessage("Refund is not allowed for this account number: " + refundRequest.getAcquirer().getAccount().getAccountNumber());
                LOGGER.error("Error code (900950): Refund is not allowed for this account number: " + refundRequest.getAcquirer().getAccount().getAccountNumber());
                return refundResponseDTO;
            }

            JwtExtractor jwtExtractor = new JwtExtractor();

            if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
                try {
                    jwtExtractor.extract(headers);
                } catch (JWTException | JSONException e) {
                    LOGGER.error("Refund jwtExtractor JWTException: " + e.getLocalizedMessage());
                    LOGGER.info("Refund Request in: " + JsonStringMapper.toJsonString(refundRequest));
                    e.printStackTrace();
                } catch (Exception e) {
                    LOGGER.error("Refund jwtExtractor exception: " + e.getLocalizedMessage());
                    LOGGER.info("Refund Request in: " + JsonStringMapper.toJsonString(refundRequest));
                    e.printStackTrace();
                }

                if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                    RefundResponseDTO refundResponse = new RefundResponseDTO();
                    refundResponse.setResponseCode(900910);
                    refundResponse.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                    return refundResponse;
                }

                if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                    RefundResponseDTO refundResponse = new RefundResponseDTO();
                    refundResponse.setResponseCode(900910);
                    refundResponse.setResponseMessage("Can not access the required resource with the provided access token. Key Type name missing.");
                    LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key Type missing.");
                    return refundResponse;
                }
            }else {
                RefundResponseDTO refundResponse = new RefundResponseDTO();
                refundResponse.setResponseCode(900401);
                refundResponse.setResponseMessage("Invalid credential");
                LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
                LOGGER.info("Request in: " + JsonStringMapper.toJsonString(refundRequest));
                return refundResponse;
            }

            String backEndUrl = "";
            backEndUrl = jwtExtractor.getKeyType().equalsIgnoreCase(EnvironmentEnum.PRODUCTION.getValue()) ? refundProdAvtEndpoint : refundSandboxAvtEndpoint;

            loggingPayloadMdl.setApiKey("1forYou-refund");
            loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());
            loggingPayloadMdl.setApiResource(backEndUrl);

            loggingPayloadMdl.setRequestInPayload(refundRequest);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);
            RefundRequestMdl refundRequestMdl = RefundRequestMapper.INSTANCE.toMdl(refundRequest);
            HttpEntity<RefundRequestMdl> entity = new HttpEntity<RefundRequestMdl>(refundRequestMdl, httpHeaders);

            loggingPayloadMdl.setRequestOutPayload(refundRequestMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            RefundResponseMdl refundResponseMdl = webService.refundService(entity, backEndUrl);

            loggingPayloadMdl.setResponseInPayload(refundResponseMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            refundResponseDTO = RefundResponseMapper.INSTANCE.toDTO(refundResponseMdl);
            refundResponseDTO.setAcquirer(refundRequest.getAcquirer());

            loggingPayloadMdl.setResponseOutPayload(refundResponseDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        }catch (Exception e){
            LOGGER.info("Request in: " + JsonStringMapper.toJsonString(refundRequest));
            LOGGER.error("Refund service exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }
        return refundResponseDTO;
    }

    private OneForUServiceInf initOneForUService(AcquirerDTO acquirer) {
        this.isAccountExists = this.accountsData.stream().anyMatch(
                acData -> acData.getAccountId().equals(acquirer.getAccount().getAccountNumber())
        );
        return this.isAccountExists ? new OneForUServiceImplTrader() : new OneForUServiceImplAvt();
    }

    public List<AccountsData> loadFile(String fileToLoad){

        if("trader".equals(fileToLoad)){
            loadAccountsData();
            return this.accountsData;
        } else if("avtRedeemTest".equals(fileToLoad)){
            loadAvtRedeemTestAccountsData();
            return this.avtTestAccountsData;
        } else if("avtTestPurchase".equals(fileToLoad)){
            loadAvtPurchaseTestAccountsData();
            return this.avtPurchaseTestAccountsData;
        }
        return this.accountsData;
    }

    private void loadAccountsData() {
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        try {
            File f = new File(accountsDataFile);
            if(f.exists() && !f.isDirectory()) {
                Object obj = parser.parse(new FileReader(accountsDataFile));
                String json_string = new Gson().toJson(obj);
                this.accountsData = mapper.readValue(json_string, new TypeReference<List<AccountsData>>() {
                });
            }
        } catch (IOException e) {
            LOGGER.error("Load Accounts Data IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            LOGGER.error("Load Accounts Data printStackTrace: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAvtRedeemTestAccountsData() {
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        try {
            File f = new File(avtRedeemTestAccountsDataFile);
            if(f.exists() && !f.isDirectory()) {
                Object obj = parser.parse(new FileReader(avtRedeemTestAccountsDataFile));
                String json_string = new Gson().toJson(obj);
                this.avtTestAccountsData = mapper.readValue(json_string, new TypeReference<List<AccountsData>>() {
                });
            }
        } catch (IOException e) {
            LOGGER.error("Load Avt Redeem Test Accounts Data IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            LOGGER.error("Load Avt Redeem Test Accounts Data ParseException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAvtPurchaseTestAccountsData() {
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        try {
            File f = new File(avtPurchaseTestAccountsDataFile);
            if(f.exists() && !f.isDirectory()) {
                Object obj = parser.parse(new FileReader(avtPurchaseTestAccountsDataFile));
                String json_string = new Gson().toJson(obj);
                this.avtPurchaseTestAccountsData = mapper.readValue(json_string, new TypeReference<List<AccountsData>>() {
                });
            }
        } catch (IOException e) {
            LOGGER.error("Load Avt Purchase Test Accounts Data IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            LOGGER.error("Load Avt Purchase Test Accounts Data ParseException: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private RefundResponseDTO jdGroupRefund(Map<String,String> headers, RefundRedemptionDTO refundRedemptionDTO){

        RefundRequestDTO refundRequest = new RefundRequestDTO();
        try {
            refundRequest.setRequestId(refundRedemptionDTO.getRequestId());
            refundRequest.setAmount(refundRedemptionDTO.getAmount());
            refundRequest.setUserId("jdGroup");

            AcquirerDTO acquirerDTO = new AcquirerDTO();
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setAccountNumber(refundRedemptionDTO.getAcquirer().getAccount().getAccountNumber());

            acquirerDTO.setAccount(accountDTO);
            acquirerDTO.setEntityTag(refundRedemptionDTO.getAcquirer().getEntityTag());
            refundRequest.setAcquirer(acquirerDTO);

        } catch (Exception e){
            LOGGER.error("Json parse error in jdGroupRefund method: " + e.getMessage());
            e.printStackTrace();
        }
        return refundService(headers, refundRequest);
    }

    public Object refundRedemptionService(Map<String,String> headers, RefundRedemptionDTO refundRedemptionDTO) throws NotFoundException{

        RefundResponseDTO refundResponseDTO = new RefundResponseDTO();
        RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl= new RefundRedemptionResponseTraderMdl();
        RefundResponseTraderDTO refundResponseTraderDTO = new RefundResponseTraderDTO();

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("1forYou-refundRedemption");
        loggingPayloadMdl.setApiResource("/refundRedemption");
        try {
            String accountNumber = refundRedemptionDTO.getAcquirer().getAccount().getAccountNumber();

            // JD group
            // and Sandbox purchase "4678-3256-0716-6615"
            // and Sandbox Redeem "1173-5928-2165-5493"
            // and production purchase "6559-2323-0232-2438"
            // and production Redeem "2713-5132-6894-4767"
            if("4678-3256-0716-6615".equals(accountNumber) ||
               "1173-5928-2165-5493".equals(accountNumber) ||
               "6559-2323-0232-2438".equals(accountNumber) ||
               "2713-5132-6894-4767".equals(accountNumber)
            ) {
                return jdGroupRefund(headers, refundRedemptionDTO);
            }

            init();

            this.isAccountExists = this.accountsData.stream().anyMatch(
                    acData -> acData.getAccountId().equals(accountNumber)
            );

            // If AVT - refund redemption is not allowed
            if (!this.isAccountExists) {
                LOGGER.info("Account number not found: " + accountNumber);
                LOGGER.info("Refund redemption is not allowed for account number : " + accountNumber);
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                throw new NotFoundException();
            }
        }catch (Exception e){
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            throw new NotFoundException();
        }


        JwtExtractor jwtExtractor = new JwtExtractor();

        if(headers.containsKey(wso2JwtKeys.xJwtAssertion)) {
            try {
                jwtExtractor.extract(headers);
            } catch (JWTException | JSONException e) {
                LOGGER.error("Refund redemption jwtExtractor JWTException: " + e.getLocalizedMessage());
                LOGGER.info("Refund redemption Request in: " + JsonStringMapper.toJsonString(refundRedemptionDTO));
                e.printStackTrace();
            } catch (Exception e) {
                LOGGER.error("Refund redemption jwtExtractor exception: " + e.getLocalizedMessage());
                LOGGER.info("Refund redemption Request in: " + JsonStringMapper.toJsonString(refundRedemptionDTO));
                e.printStackTrace();
            }

            if (null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()) {
                refundResponseTraderDTO.setResponseCode(900910);
                refundResponseTraderDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
                return refundResponseTraderDTO;
            }

            if (null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()) {
                refundResponseTraderDTO.setResponseCode(900910);
                refundResponseTraderDTO.setResponseMessage("Can not access the required resource with the provided access token. Key Type name missing.");
                LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key Type missing.");
                return refundResponseTraderDTO;
            }
        } else {
            refundResponseTraderDTO.setResponseCode(900401);
            refundResponseTraderDTO.setResponseMessage("Invalid credential");
            LOGGER.error("Error code (900401): Invalid credential, missing x-jwt-assertion from header");
            LOGGER.info("Request in: " + JsonStringMapper.toJsonString(refundRedemptionDTO));
            return refundResponseTraderDTO;
        }

        try{
            loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

            loggingPayloadMdl.setRequestInPayload(refundRedemptionDTO);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);

            Long seqNum = getSequenceNumber(httpHeaders, oneForYouTraderEsbProd + "/sequenceNumber", refundRedemptionDTO.getAcquirer().getAccount().getAccountNumber(), refundRedemptionDTO.getRequestId(), "1forYou-refundRedemption");

            RefundRedemptionRequestTraderMdl refundRedemptionRequestTraderMdl = RefundRedemptionMapper.INSTANCE.toMdl(refundRedemptionDTO);
            List<AcquirerTraderMdl> acquirerReference= new ArrayList<>();
            AcquirerTraderMdl acquirerTraderMdl = new AcquirerTraderMdl();
            acquirerTraderMdl.setKey("clientReference");
            acquirerTraderMdl.setValue("MYCUST-X404");
            acquirerReference.add(acquirerTraderMdl);
            refundRedemptionRequestTraderMdl.setAcquirerReference(acquirerReference);
            refundRedemptionRequestTraderMdl.setSequenceNumber(seqNum);


            HttpEntity<RefundRedemptionRequestTraderMdl> entity = new HttpEntity<RefundRedemptionRequestTraderMdl>(refundRedemptionRequestTraderMdl, httpHeaders);


            loggingPayloadMdl.setRequestOutPayload(refundRedemptionRequestTraderMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            refundRedemptionResponseTraderMdl = webService.oneForYouTraderRefundRedemption(entity, oneForYouTraderEsbProd + "/refundRedemption");
            loggingPayloadMdl.setResponseInPayload(refundRedemptionResponseTraderMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            refundResponseDTO =RefundRedemptionMapper.INSTANCE.toDTO(refundRedemptionResponseTraderMdl);

            refundResponseTraderDTO = RefundRedemptionMapper.INSTANCE.toTraderDTO(refundResponseDTO);
            refundResponseTraderDTO.setAcquirer(refundRedemptionDTO.getAcquirer());
            refundResponseTraderDTO.setRequestId(refundRedemptionDTO.getRequestId());
            refundResponseTraderDTO.getVoucher().setAmount(null);

            refundResponseDTO.setRequestId(refundRedemptionDTO.getRequestId());
            refundResponseDTO.setAcquirer(refundRedemptionDTO.getAcquirer());

            loggingPayloadMdl.setResponseOutPayload(refundResponseTraderDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        }catch (Exception e){
            LOGGER.info("Request in: " + JsonStringMapper.toJsonString(refundRedemptionDTO));
            LOGGER.error("Refund redemption service exception: " + e.getLocalizedMessage());
            LOGGER.info("Refund redemption service exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            LOGGER.info("Refund redemption payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        return refundResponseTraderDTO;
    }

    public Long getSequenceNumber(HttpHeaders headers, String seqUrl, String accountNumber, String sequenceNumber, String messageType){
        SequenceMdl sequenceMdl=new SequenceMdl();
        sequenceMdl.setAccountNumber(accountNumber);
        sequenceMdl.setSequenceNumber(sequenceNumber);
        sequenceMdl.setMessageType(messageType);

        HttpEntity<SequenceMdl> purchaseRequestEntity = new HttpEntity<SequenceMdl>(sequenceMdl, headers);
        Long sn = webService.oneForYouTraderSequenceNumber(purchaseRequestEntity, seqUrl);
        return sn;
    }

    public ReverseResponseTraderDTO reversalRedemptionService(Map<String,String> headers, ReverseRedemptionDTO reverseRedemptionDTO) throws NotFoundException{

        init();

        LoggingPayloadMdl loggingPayloadMdl = new LoggingPayloadMdl();
        loggingPayloadMdl.setApiKey("1forYou-reversalRedemption");
        loggingPayloadMdl.setApiResource("/reversalRedemption");
        ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl = new ReversalsRedemptionResponseTraderMdl();
        ReverseResponseTraderDTO reverseResponseTraderDTO = new ReverseResponseTraderDTO();

        try {
            String accountNumber = reverseRedemptionDTO.getAcquirer().getAccount().getAccountNumber();
            this.isAccountExists = this.accountsData.stream().anyMatch(
                    acData -> acData.getAccountId().equals(accountNumber)
            );

            // If AVT - reversal redemption is not allowed
            if (!this.isAccountExists) {
                LOGGER.info("Account number not found: " + accountNumber);
                LOGGER.info("Reversal redemption is not allowed for Account number: " + accountNumber);
                LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
                throw new NotFoundException();
            }
        }catch (Exception e){
            LOGGER.info("Reversal redemption payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
            throw new NotFoundException();
        }

        JwtExtractor jwtExtractor = new JwtExtractor();

        try{
            jwtExtractor.extract(headers);
        } catch (JWTException | JSONException e) {
            LOGGER.error("Reversal redemption jwtExtractor JWTException: " + e.getLocalizedMessage());
            LOGGER.info("Reversal redemption Request in: " + JsonStringMapper.toJsonString(reverseRedemptionDTO));
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.error("Reversal redemption jwtExtractor exception: " + e.getLocalizedMessage());
            LOGGER.info("Reversal redemption Request in: " + JsonStringMapper.toJsonString(reverseRedemptionDTO));
            e.printStackTrace();
        }

        if(null == jwtExtractor.getOrganization() || jwtExtractor.getOrganization().isEmpty()){
            reverseResponseTraderDTO.setResponseCode(900910);
            reverseResponseTraderDTO.setResponseMessage("Can not access the required resource with the provided access token. Organization name missing.");
            LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Organization name missing.");
            return reverseResponseTraderDTO;
        }

        if(null == jwtExtractor.getKeyType() || jwtExtractor.getKeyType().isEmpty()){
            reverseResponseTraderDTO.setResponseCode(900910);
            reverseResponseTraderDTO.setResponseMessage("Can not access the required resource with the provided access token. Key Type name missing.");
            LOGGER.error("Error code (900910): Can not access the required resource with the provided access token. Key Type missing.");
            return reverseResponseTraderDTO;
        }


        try{
            loggingPayloadMdl.setOrganization(jwtExtractor.getOrganization());
            loggingPayloadMdl.setKeyType(jwtExtractor.getKeyType());

            loggingPayloadMdl.setRequestInPayload(reverseRedemptionDTO);
            loggingPayloadMdl.setRequestInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            OneForUServiceImplTrader oneForUService = new OneForUServiceImplTrader();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);

            Long seqNum = getSequenceNumber(httpHeaders, oneForYouTraderEsbProd + "/sequenceNumber", reverseRedemptionDTO.getAcquirer().getAccount().getAccountNumber(), reverseRedemptionDTO.getRequestId(), "1forYou-reverseRedemption");

            ReverseRedemptionRequestTraderMdl reversalRedemptionRequestTraderMdl = ReverseRedemptionMapper.INSTANCE.toMdl(reverseRedemptionDTO);
            List<AcquirerTraderMdl> acquirerReference= new ArrayList<>();
            AcquirerTraderMdl acquirerTraderMdl = new AcquirerTraderMdl();
            acquirerTraderMdl.setKey("clientReference");
            acquirerTraderMdl.setValue("MYCUST-X404");
            acquirerReference.add(acquirerTraderMdl);
            reversalRedemptionRequestTraderMdl.setAcquirerReference(acquirerReference);
            reversalRedemptionRequestTraderMdl.setSequenceNumber(seqNum);

            HttpEntity<ReverseRedemptionRequestTraderMdl> entity = new HttpEntity<ReverseRedemptionRequestTraderMdl>(reversalRedemptionRequestTraderMdl, httpHeaders);

            loggingPayloadMdl.setRequestOutPayload(reversalRedemptionRequestTraderMdl);
            loggingPayloadMdl.setRequestOutDateTime(FlashDates.currentDateTimeFlashFormatInString());

            reversalsRedemptionResponseTraderMdl = webService.oneForYouTraderReverseRedemption(entity, oneForYouTraderEsbProd + "/reverseRedemption");

            loggingPayloadMdl.setResponseInPayload(reversalsRedemptionResponseTraderMdl);
            loggingPayloadMdl.setResponseInDateTime(FlashDates.currentDateTimeFlashFormatInString());

            reverseResponseTraderDTO = ReverseRedemptionMapper.INSTANCE.toDTO(reversalsRedemptionResponseTraderMdl);
            reverseResponseTraderDTO.setRequestId(reverseRedemptionDTO.getRequestId());
            reverseResponseTraderDTO.getAcquirer().setEntityTag(reverseRedemptionDTO.getAcquirer().getEntityTag());
            reverseResponseTraderDTO.getAcquirer().getAccount().setAccountNumber(Long.valueOf(reverseRedemptionDTO.getAcquirer().getAccount().getAccountNumber()));


            loggingPayloadMdl.setResponseOutPayload(reverseResponseTraderDTO);
            loggingPayloadMdl.setResponseOutDateTime(FlashDates.currentDateTimeFlashFormatInString());
        }catch (Exception e){
            LOGGER.info("Request in: " + JsonStringMapper.toJsonString(reverseRedemptionDTO));
            LOGGER.error("Reverse redemption service exception: " + e.getLocalizedMessage());
            LOGGER.info("Reverse redemption service exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            LOGGER.info("payload: " + JsonStringMapper.toJsonString(loggingPayloadMdl));
        }

        return reverseResponseTraderDTO;

    }

}
