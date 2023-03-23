package co.za.flash.esb.oneforyou.dto.shared;

import lombok.Data;

import java.util.List;

@Data
public class CustomerContactObDTO {
    public List<String> mechanism;
    public String address;
}
