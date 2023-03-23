package co.za.flash.esb.avtrouting.controller;

import co.za.flash.esb.avtrouting.model.dto.request.RedeemRequestDTO;
import co.za.flash.esb.avtrouting.service.RedeemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/1.0.0/v1")
@Api(tags = "OpenapiConditionalAvtRouting", description = "redeem")
public class RedeemController {

    @Autowired
    RedeemService redeemService;

    @PostMapping("/transaction/onevoucher/redeem")
    public Object redeem (@RequestHeader Map<String,String> headers, @Valid @RequestBody RedeemRequestDTO redeemRequest){
        return  redeemService.redeem(headers, redeemRequest);
    }
}
