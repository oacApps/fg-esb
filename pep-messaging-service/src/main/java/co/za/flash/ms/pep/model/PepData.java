package co.za.flash.ms.pep.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PepData {
    private String branchNo;
    private String voucherProductCode;
    private String cardCode;
    private String sellDate;
}
