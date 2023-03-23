package co.za.flash.esb.cellular.controller;

import co.za.flash.esb.cellular.dto.CellularRequestDTO;
import co.za.flash.esb.cellular.service.CellularService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "cellular")
public class CellularController {

    @Autowired
    CellularService cellularService;

    @PostMapping("/pinless/purchase")
    public Object pinLessPurchase(@RequestHeader Map<String, String> headers,
                                  @RequestBody CellularRequestDTO cellularRequestDTO) {
        Object cellularResponse = cellularService.clientService(headers, cellularRequestDTO, true);
        return cellularResponse;
    }

    @PostMapping("/pin/purchase")
    public Object pinPurchase(@RequestHeader Map<String, String> headers,
                              @Valid @RequestBody CellularRequestDTO cellularRequestDTO) {
        Object cellularResponse = cellularService.clientService(headers, cellularRequestDTO, false);
        return cellularResponse;
    }

    /*@PostMapping("/pin/purchase/eeziairtime")
    public Object pinPurchaseEeziAirTime(@RequestHeader Map<String, String> headers,
                              @Valid @RequestBody CellularRequestDTO cellularRequestDTO) {
        cellularRequestDTO.setTrans_pin_type(1L);
        cellularRequestDTO.setNetwork("EEZIAIRTIME");
        Object cellularResponse = cellularService.clientService(headers, cellularRequestDTO);
        return cellularResponse;
    }*/
}
