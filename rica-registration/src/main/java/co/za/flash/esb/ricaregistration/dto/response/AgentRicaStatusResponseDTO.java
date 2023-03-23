package co.za.flash.esb.ricaregistration.dto.response;

import lombok.Data;

@Data
public class AgentRicaStatusResponseDTO {
    private String agentMsisdn;
    private boolean canRica;
}
