package co.za.flash.esb.ricaregistration.model.request;

import lombok.Data;

import java.util.List;

@Data
public class SimCardInfoRequestMdl {
    List<String> iccids;
}
