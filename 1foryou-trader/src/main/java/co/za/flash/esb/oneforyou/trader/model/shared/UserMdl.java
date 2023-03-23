package co.za.flash.esb.oneforyou.trader.model.shared;

import lombok.Data;

@Data
public class UserMdl {
    public String msisdn;
    public String purseAccountNumber;
    public String guid;
    public AuthenticationMdl authentication;
    public String authUserName;
    public String authPassWord;
    public String authEmail;
}
