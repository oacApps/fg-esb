package co.za.flash.esb.ricaregistration.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SimCardInfoRequestDTO {
    List<String> iccids;
}
