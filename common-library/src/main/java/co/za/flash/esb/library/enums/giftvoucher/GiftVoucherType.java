package co.za.flash.esb.library.enums.giftvoucher;

public enum GiftVoucherType {

    SPOTIFY (1, "SPOTIFY"),
    NETFLIX (2, "NETFLIX"),
    UBER (3, "UBER"),
    ONE_FOR_YOU (4,"1FORYOU"),
    ROBLOX(5, "ROBLOX");


    private final long value;
    private final String type;
    GiftVoucherType(long value, String type) {
        this.value = value;
        this.type = type;
    }
    public long getValue(){ return this.value;}

    public String getType() {
        return type;
    }
}
