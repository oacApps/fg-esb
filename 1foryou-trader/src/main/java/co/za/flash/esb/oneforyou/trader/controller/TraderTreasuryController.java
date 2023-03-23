package co.za.flash.esb.oneforyou.trader.controller;

import co.za.flash.esb.oneforyou.trader.dto.SequenceDTO;
import co.za.flash.esb.oneforyou.trader.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.request.RefundRedemptionRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.request.ReversalsRedemptionRequestDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RefundRedemptionResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.ReversalsRedemptionResponseDTO;
import co.za.flash.esb.oneforyou.trader.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "1forYouTrader", description = "purchase and redeem")
public class TraderTreasuryController {

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    RedeemService redeemService;

    @Autowired
    RefundService refundService;

    @Autowired
    ReversalsService reversalsService;

    @Autowired
    SequenceService sequenceService;

    @PostMapping("/purchase")
    public PurchaseResponseDTO purchase (@RequestHeader Map<String,String> headers, @Valid @RequestBody PurchaseRequestDTO purchase){
        return  purchaseService.purchaseSvr(headers, purchase);
    }
    @PostMapping("/redeem")
    public RedeemResponseMatchWsoDTO redeem (@RequestHeader Map<String,String> headers, @Valid @RequestBody RedeemRequestDTO redeemRequestDTO){
        return  redeemService.redeemSvr(headers, redeemRequestDTO);
    }
    @PostMapping("/refundRedemption")
    public RefundRedemptionResponseMatchWsoDTO refundRedemption (@RequestHeader Map<String,String> headers, @Valid @RequestBody RefundRedemptionRequestDTO refundRedemptionRequestDTO){
        return  refundService.refundSvr(headers, refundRedemptionRequestDTO);
    }
    @PostMapping("/reverseRedemption")
    public ReversalsRedemptionResponseDTO reverseRedemption (@RequestHeader Map<String,String> headers, @Valid @RequestBody ReversalsRedemptionRequestDTO reversalsRedemptionRequestDTO){
        return  reversalsService.reversalsSvr(headers, reversalsRedemptionRequestDTO);
    }
    @PostMapping("/sequenceNumber")
    public Long sequenceNumber(@RequestBody SequenceDTO sequenceDTO){
        return sequenceService.sequenceNumber(sequenceDTO);
    }
}

