package co.za.flash.esb.oneforyou.controller;

import co.za.flash.esb.library.pojo.AccountsData;
import co.za.flash.esb.oneforyou.dto.request.*;
import co.za.flash.esb.oneforyou.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.oneforyou.dto.response.RefundResponseDTO;
import co.za.flash.esb.oneforyou.dto.response.ReverseResponseTraderDTO;
import co.za.flash.esb.oneforyou.exceptions.NotFoundException;
import co.za.flash.esb.oneforyou.service.OneForYouService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "1foryou", description = "purchase and redeem")
public class OneForYouController {

    @Autowired
    OneForYouService oneForYouService;

    @GetMapping("/load/{fileToLoad}")
    public List<AccountsData> loadAccountsFile(@PathVariable String fileToLoad){
        return oneForYouService.loadFile(fileToLoad);
    }

    @PostMapping("/purchase")
    public PurchaseResponseDTO purchase (@RequestHeader Map<String,String> headers, @Valid @RequestBody PurchaseRequestDTO purchase){
        return  oneForYouService.purchaseService(headers, purchase);
    }

    @PostMapping("/redeem")
    public Object redeem (@RequestHeader Map<String,String> headers, @Valid @RequestBody RedeemRequestDTO redeem){
        return  oneForYouService.redeemService(headers, redeem);
    }

    @PostMapping("/refund")
    public RefundResponseDTO refund (@RequestHeader Map<String,String> headers, @Valid @RequestBody RefundRequestDTO refund){
        return  oneForYouService.refundService(headers, refund);
    }

    @PostMapping("/refundRedemption")
    public Object refundRedemption(@RequestHeader Map<String,String> headers, @RequestBody RefundRedemptionDTO refundRedemptionObject) throws NotFoundException {
        return oneForYouService.refundRedemptionService(headers, refundRedemptionObject);
    }

    @PostMapping("/reverseRedemption")
    public ReverseResponseTraderDTO reverseRedemption(@RequestHeader Map<String,String> headers, @RequestBody ReverseRedemptionDTO reverseRedemptionDTO) throws NotFoundException {
        return oneForYouService.reversalRedemptionService(headers, reverseRedemptionDTO);
    }
}
