package co.za.flash.esb.library.enums.cellular;

public enum TransPinResponseType {
    PIN_LESS (0, "PINLESS"),
    PIN (1, "PIN");

    private final int serialNumber;
    private final String value;

    TransPinResponseType(int serialNumber, String value) {
        this.serialNumber = serialNumber;
        this.value = value;
    }

    public String getValue(){ return this.value;}

    public int getSerialNumber() {
        return serialNumber;
    }


}
