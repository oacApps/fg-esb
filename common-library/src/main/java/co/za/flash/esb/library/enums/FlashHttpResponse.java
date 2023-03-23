package co.za.flash.esb.library.enums;

public enum FlashHttpResponse {

    UNAUTHORIZED ("Unauthorized");

    private final String value;

    FlashHttpResponse(String value) {
        this.value = value;
    }

    public String getValue(){ return this.value;}
}
