package co.za.flash.esb.oneforyou.model.shared;

import lombok.Data;

@Data
public class UserTTMdl {
    public String msisdn;
    public String purseAccountNumber;
    public String guid;
    public AuthenticationTTMdl authentication;
    public String authUserName;
    public String authPassWord;
    public String authEmail;
}
