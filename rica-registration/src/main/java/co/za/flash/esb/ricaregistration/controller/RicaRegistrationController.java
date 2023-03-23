package co.za.flash.esb.ricaregistration.controller;

import co.za.flash.esb.ricaregistration.dto.request.AgentRicaStatusRequestDTO;
import co.za.flash.esb.ricaregistration.dto.request.RegistrationsRequestDTO;
import co.za.flash.esb.ricaregistration.dto.request.SimCardInfoRequestDTO;
import co.za.flash.esb.ricaregistration.service.AgentRicaStatusService;
import co.za.flash.esb.ricaregistration.service.RicaRegistrationService;
import co.za.flash.esb.ricaregistration.service.SimCardInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/1.0.0")
@Api(tags = "RicaRegistration")
public class RicaRegistrationController {

    @Autowired
    AgentRicaStatusService agentRicaStatusService;

    @Autowired
    RicaRegistrationService ricaRegistrationService;

    @Autowired
    SimCardInfoService simCardInfoService;

    @PostMapping("/registrations")
    public Object registrations(@RequestHeader Map<String, String> headers, @RequestBody RegistrationsRequestDTO registrationsRequestDTO){
        return ricaRegistrationService.registration(headers, registrationsRequestDTO);
    }
    @PostMapping("/getagentricastatus")
    public Object agentRicaStatus(@RequestHeader Map<String, String> headers, @RequestBody AgentRicaStatusRequestDTO agentricaStatusRequestDTO){
        return agentRicaStatusService.agentRicaStatus(headers, agentricaStatusRequestDTO);
    }
    @PostMapping("/getsimcardinfo")
    public Object simCardInfo(@RequestHeader Map<String, String> headers, @RequestBody SimCardInfoRequestDTO simCardInfoRequestDTO){
        return simCardInfoService.simCardInfo(headers, simCardInfoRequestDTO);
    }
}
