package co.za.flash.esb.aggregation.dstv.controller;

import co.za.flash.esb.aggregation.dstv.dto.request.LookUpRequestDTO;
import co.za.flash.esb.aggregation.dstv.dto.request.PaymentRequestDTO;
import co.za.flash.esb.aggregation.dstv.service.LookUpService;
import co.za.flash.esb.aggregation.dstv.service.PaymentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "DSTV")
public class DSTVController {

    @Autowired
    LookUpService lookUpService;

    @Autowired
    PaymentService paymentService;

    @PostMapping("/lookup")
    public Object lookup(@RequestHeader Map<String, String> headers, @RequestBody LookUpRequestDTO lookUpRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return lookUpService.service(headers, lookUpRequestDTO, test);
    }
    @PostMapping("/payment")
    public Object payment(@RequestHeader Map<String, String> headers, @RequestBody PaymentRequestDTO paymentRequestDTO, @RequestParam(defaultValue = "false") boolean test){
        return paymentService.service(headers, paymentRequestDTO, test);
    }
}
