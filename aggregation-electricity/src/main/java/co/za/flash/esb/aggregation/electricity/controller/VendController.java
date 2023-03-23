package co.za.flash.esb.aggregation.electricity.controller;

import co.za.flash.esb.aggregation.electricity.dto.*;
import co.za.flash.esb.aggregation.electricity.dto.common.ElectricityRequestDTO;
import co.za.flash.esb.aggregation.electricity.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "Electricity")
public class VendController {

    @Autowired
    LookUpService lookUpService;
    @Autowired
    LookUpCoCtService lookUpCoCtService;
    @Autowired
    CopyTokenService copyTokenService;
    @Autowired
    BlindVendService blindVendService;

    @Autowired
    VendTsweneService vendTsweneService;
    @Autowired
    VendService vendService;
    @Autowired
    VendEscomService vendEscomService;

    @Autowired
    VendCoctService vendCoctService;

    @PostMapping("/vend/coct")
    public Object vendCoCt(@RequestHeader Map<String, String> headers, @RequestBody ElectricityRequestDTO electricityRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return vendCoctService.service(headers, electricityRequestDTO, test);
    }
    @PostMapping("/vend/tshwane")
    public Object vendTshwane(@RequestHeader Map<String, String> headers, @RequestBody ElectricityRequestDTO electricityRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return vendTsweneService.service(headers, electricityRequestDTO, test);
    }
    @PostMapping("/vend/default")
    public Object vendDefault(@RequestHeader Map<String, String> headers, @RequestBody ElectricityRequestDTO electricityRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return vendService.service(headers, electricityRequestDTO, test);
    }
    @PostMapping("/vend/eskom")
    public Object vendEskom(@RequestHeader Map<String, String> headers, @RequestBody ElectricityRequestDTO electricityRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return vendEscomService.service(headers, electricityRequestDTO, test);
    }
    @PostMapping("/blindvend")
    public Object blindVend(@RequestHeader Map<String, String> headers, @RequestBody BlindVendRequestDTO blindVendRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return blindVendService.service(headers, blindVendRequestDTO, test);
    }
    @PostMapping("/copytoken")
    public Object copyToken(@RequestHeader Map<String, String> headers, @RequestBody CopyTokenRequestDTO copyTokenRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return copyTokenService.service(headers, copyTokenRequestDTO, test);
    }
    @PostMapping("/lookup")
    public Object lookup(@RequestHeader Map<String, String> headers, @RequestBody LookupRequestDTO lookupRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return lookUpService.service(headers, lookupRequestDTO, test);
    }
    @PostMapping("/lookup/coct")
    public Object lookupCoCt(@RequestHeader Map<String, String> headers, @RequestBody LookupCoCtRequestDTO lookupCoCtRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return lookUpCoCtService.service(headers, lookupCoCtRequestDTO, test);
    }
}
