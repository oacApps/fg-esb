package co.za.flash.esb.library.pojo;

import lombok.Data;

@Data
public class AccountsData {
    private String accountId;
    private boolean enabled;
    private String environment;
    private String organization;
}
