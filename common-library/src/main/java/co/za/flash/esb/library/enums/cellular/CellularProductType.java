package co.za.flash.esb.library.enums.cellular;


public enum CellularProductType {
    AIRTIME (00, "Airtime"),
    DATA (20, "Data"),
    Bundle (30, "Bundle");

    private final long value;
    private final String type;
    CellularProductType(long value, String type) {
        this.value = value;
        this.type = type;
    }
    public long getValue(){ return this.value;}

    public String getType() {
        return type;
    }
}
