package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class AuthenticationTTMdl {
    private boolean authenticated;
    private PinBlockTTMdl pinBlock;
}
