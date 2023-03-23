package co.za.flash.esb.library.enums.cellular;

public enum CellularNetwork {
    VODA_COM (1, "VODACOM", "vcom"),
    MTN (3, "MTN", "mtn"),
    CELL_C (7, "CELL C", "cellc"),
    VIRGIN_MOBILE(8, "VIRGIN MOBILE", "vmob"),
    TELKOM_MOBILE (28, "TELKOM", "tkom");

    private final int serialNumber;
    private final String value;
    private final String code;

    CellularNetwork(int serialNumber, String value, String code) {
        this.serialNumber = serialNumber;
        this.value = value;
        this.code = code;
    }

    public String getValue(){ return this.value;}

    public String getCode(){ return this.code;}

    public long getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString(){
        return value;
    }

    public static CellularNetwork fromString(String network){
        if(network != null){
            for(CellularNetwork cn : CellularNetwork.values()){
                if(cn.value.equalsIgnoreCase(network)){
                    return cn;
                }
            }
        }
        return null;
    }
}
