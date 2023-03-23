package co.za.flash.esb.library.enums;

public enum HeadersKey {
    AUTHORIZATION("authorization"),
    X_JWT_ASSERTION("x-jwt-assertion"),
    ACCEPT("accept"),
    CONTENT_TYPE("content-type"),
    USER_AGENT("user-agent"),
    MEDIA_TYPE_APPLICATION_JSON("application/json");

    private final String value;

    HeadersKey(String value) {
        this.value = value;
    }

    public String getValue(){ return this.value;}
}
