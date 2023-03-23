package co.za.flash.esb.avtrouting;

public enum Accounts {

    HOLLYWOOD("HOLLYWOOD B", "6105843894"),
    RAGING("RAGING RIVE", "6106201852"),
    WAZAA("WAZAA-001", "6100000950");

    private String accountNumber;
    private String shopName;


    Accounts(String accountNumber, String shopName) {
        this.accountNumber = accountNumber;
        this.shopName = shopName;
    }
}
