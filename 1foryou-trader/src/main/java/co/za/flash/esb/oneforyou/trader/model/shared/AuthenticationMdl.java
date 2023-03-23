package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

@Data
public class AuthenticationMdl {
    private boolean authenticated;
    private PinBlockMdl pinBlock;
}
