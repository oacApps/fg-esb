package co.za.flash.esb.library.enums;

public enum SecurityError {

    ERR_USR_FETCH_FROM_TOKEN ("An error occurred while fetching Username from Token"),
    TOKEN_EXPIRE ("The token has expired"),
    AUTH_FAILED("Authentication Failed. Username or Password not valid."),
    AUTH_TOKEN_NOT_FOUND("Couldn't find authentication token, header will be ignored"),
    USER_AUTHENTICATED("authenticated , setting security context");

    private final String value;

    SecurityError(String value) {
        this.value = value;
    }

    public String getValue(){ return this.value;}
}
