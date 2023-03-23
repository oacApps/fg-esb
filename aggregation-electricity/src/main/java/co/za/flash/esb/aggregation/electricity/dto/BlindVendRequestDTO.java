package co.za.flash.esb.aggregation.electricity.dto;

import co.za.flash.esb.aggregation.electricity.dto.common.ElectricityRequestDTO;
import lombok.Data;

@Data
public class BlindVendRequestDTO extends ElectricityRequestDTO {
    private String key_revision_num;
    private String tariff_index;
    private String supply_group_code;
    private String algorithm_technology;
    private String token_technology;
}
