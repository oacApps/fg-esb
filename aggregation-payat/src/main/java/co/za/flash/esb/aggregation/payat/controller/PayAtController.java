package co.za.flash.esb.aggregation.payat.controller;

import co.za.flash.esb.aggregation.payat.dto.request.CompleteRequestDTO;
import co.za.flash.esb.aggregation.payat.dto.request.InitiateRequestDTO;
import co.za.flash.esb.aggregation.payat.dto.request.LookUpRequestDTO;
import co.za.flash.esb.aggregation.payat.dto.request.ReversalRequestDTO;
import co.za.flash.esb.aggregation.payat.service.CompleteService;
import co.za.flash.esb.aggregation.payat.service.InitiateService;
import co.za.flash.esb.aggregation.payat.service.LookUpService;
import co.za.flash.esb.aggregation.payat.service.ReversalService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "Pay at")
public class PayAtController {

    @Autowired
    LookUpService lookUpService;
    @Autowired
    InitiateService initiateService;
    @Autowired
    CompleteService completeService;
    @Autowired
    ReversalService reversalService;

    @PostMapping("/lookup")
    public Object lookup(@RequestHeader Map<String, String> headers, @RequestBody LookUpRequestDTO lookUpReqDTO, @RequestParam(defaultValue = "false") boolean test){
        return lookUpService.service(headers, lookUpReqDTO, test);
    }
    @PostMapping("/initiate")
    public Object initiate(@RequestHeader Map<String, String> headers, @RequestBody InitiateRequestDTO initiateReqDTO, @RequestParam(defaultValue = "false") boolean test){
        return initiateService.service(headers, initiateReqDTO, test);
    }
    @PostMapping("/complete")
    public Object complete(@RequestHeader Map<String, String> headers, @RequestBody CompleteRequestDTO completeReqDTO, @RequestParam(defaultValue = "false") boolean test){
        return completeService.service(headers, completeReqDTO, test);
    }
    @PostMapping("/reversal")
    public Object reversal(@RequestHeader Map<String, String> headers, @RequestBody ReversalRequestDTO reversalReqDTO, @RequestParam(defaultValue = "false") boolean test){
        return reversalService.service(headers, reversalReqDTO, test);
    }


}
