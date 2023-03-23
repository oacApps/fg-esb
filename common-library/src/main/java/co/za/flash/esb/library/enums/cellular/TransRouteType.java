package co.za.flash.esb.library.enums.cellular;

public enum TransRouteType {
    LIVE_ROUTE (0, "LIVE Route"),
    TEST_ROUTE (1, "TEST Route");

    private final int serialNumber;
    private final String value;

    TransRouteType(int serialNumber, String value) {
        this.serialNumber = serialNumber;
        this.value = value;
    }

    public String getValue(){ return this.value;}

    public int getSerialNumber() {
        return serialNumber;
    }
}
