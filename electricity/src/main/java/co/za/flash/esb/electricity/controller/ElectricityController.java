package co.za.flash.esb.electricity.controller;

import co.za.flash.esb.electricity.dto.request.LookupRequestDTO;
import co.za.flash.esb.electricity.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.electricity.dto.response.LookupResponseDTO;
import co.za.flash.esb.electricity.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.electricity.service.ElectricityService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "Electricity", description = "purchase and lookup")
public class ElectricityController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ElectricityService electricityService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/lookup")
    public LookupResponseDTO lookup(@RequestHeader Map<String, String> headers, @Valid @RequestBody LookupRequestDTO lookup){
        return electricityService.lookupService(headers, lookup);
    }
    
    @PostMapping("/purchase")
    public PurchaseResponseDTO purchase (@RequestHeader Map<String,String> headers, @Valid @RequestBody PurchaseRequestDTO purchase){
        return electricityService.purchaseRequestService(headers,purchase);
    }
}


