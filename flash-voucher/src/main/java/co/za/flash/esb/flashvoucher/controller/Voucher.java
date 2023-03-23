package co.za.flash.esb.flashvoucher.controller;

import co.za.flash.esb.flashvoucher.dto.RequestDTO;
import co.za.flash.esb.flashvoucher.dto.ResponseDTO;
import co.za.flash.esb.flashvoucher.service.VoucherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "Flash voucher", description = "purchase")
public class Voucher {

    @Autowired
    VoucherService voucherService;

    @PostMapping("/purchase")
    public ResponseDTO purchase (@RequestHeader Map<String,String> headers, @RequestBody RequestDTO purchase){
        return  voucherService.purchase(headers, purchase);
    }
}
