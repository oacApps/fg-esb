package co.za.flash.esb.electricity.dto.shared;

import co.za.flash.esb.library.enums.CustomerContactMechanism;
import lombok.Data;

import java.util.List;

@Data
public class CustomerContactDTO {
    //private enum mechanism{
      //  SMS,EMAIL};
    private List <CustomerContactMechanism> mechanism;
    private String address;
}
