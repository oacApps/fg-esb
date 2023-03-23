package co.za.flash.esb.giftvoucher.controller;

import co.za.flash.esb.giftvoucher.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.giftvoucher.dto.response.GetVouchersInfoResponseDTO;
import co.za.flash.esb.giftvoucher.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.giftvoucher.dto.response.PurchaseResponseOneForYouDTO;
import co.za.flash.esb.giftvoucher.mapper.PurchaseResponseMapper;
import co.za.flash.esb.giftvoucher.mapper.PurchaseResponseOneForYouMapper;
import co.za.flash.esb.giftvoucher.restservice.ConsumeWebService;
import co.za.flash.esb.giftvoucher.service.GiftVoucherService;
import co.za.flash.esb.library.JsonStringMapper;
import co.za.flash.esb.library.jwt.JWTException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.json.JSONException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "GiftVoucher", description = "purchase")
public class GiftVoucherController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GiftVoucherService giftVoucherService;

    @Autowired
    ConsumeWebService webService;

    @Value("${flash.accounts.data.json.file}")
    private String voucherInfoDataFile;

    List<GetVouchersInfoResponseDTO> vouchersInfoData = new ArrayList<>();

    @PostMapping("/purchase")
    public Object purchase(@RequestHeader Map<String, String> headers, @Valid @RequestBody PurchaseRequestDTO purchaseRequestDTO, BindingResult bindingResult)  {

        List<FieldError> bindingErrors = bindingResult.getFieldErrors();
        if(!bindingErrors.isEmpty()){
            LOGGER.warn(bindingErrors.toString());
            PurchaseResponseDTO responseObj = new PurchaseResponseDTO();
            responseObj.setResponseCode(400);
            responseObj.setResponseMessage("Bad request");
            //LOGGER.debug("Response out payload: Null object received " + responseObj.toString());
            LOGGER.info("Request in payload: " + JsonStringMapper.toJsonString( purchaseRequestDTO));
            return ResponseEntity.badRequest().body(responseObj);
        }
        PurchaseResponseDTO purchaseResponse = new PurchaseResponseDTO();
        try {
            purchaseResponse = giftVoucherService.purchaseRequestService(headers, purchaseRequestDTO);
            purchaseResponse.setResponseTime("");
        }catch (JSONException | JWTException e){
            LOGGER.error(e.getMessage());
            LOGGER.info("Request in payload: " + JsonStringMapper.toJsonString( purchaseRequestDTO));
        }

        if(null != purchaseResponse.getVoucher() && null == purchaseResponse.getVoucher().getExpiryDate()) {
            purchaseResponse.getVoucher().setExpiryDate("null");
        }

        // Because current wso2 return serialNumber as long int for 1foryou
        if(purchaseRequestDTO.getVoucherType().toLowerCase().equals("1foryou")){
            PurchaseResponseOneForYouDTO responseOneForYouDTO = PurchaseResponseOneForYouMapper.INSTANCE.toOneForYouDTO(purchaseResponse);
            return responseOneForYouDTO;
        }
        return purchaseResponse;
    }


    //NO PARAMETERS TO PASS
    @GetMapping("/getvouchersinfo")
    public List<GetVouchersInfoResponseDTO> getVoucher()
    {
        if (this.vouchersInfoData.isEmpty()) {
            loadVouchersInfoData();
        }
        return this.vouchersInfoData;
    }
    private void loadVouchersInfoData(){
        ObjectMapper mapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(voucherInfoDataFile));
            String json_string = new Gson().toJson(obj);
            this.vouchersInfoData = mapper.readValue(json_string, new TypeReference<List<GetVouchersInfoResponseDTO>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
