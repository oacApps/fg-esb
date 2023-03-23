package co.za.flash.esb.ricaregistration.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SimCardInfoResponseDTO {
    List<SimCard> simcards;
    List<String> iccidsNotFound;
}
