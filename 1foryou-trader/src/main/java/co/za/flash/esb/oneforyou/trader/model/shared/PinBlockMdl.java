package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

import java.util.List;

@Data
public class PinBlockMdl {
    private String format;
    private List<String> data;
}
