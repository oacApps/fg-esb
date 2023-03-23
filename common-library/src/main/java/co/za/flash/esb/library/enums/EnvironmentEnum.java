package co.za.flash.esb.library.enums;

public enum EnvironmentEnum {
    PRODUCTION ("Production"),
    SANDBOX ("Sandbox");

    private final String value;

    EnvironmentEnum(String value) {
        this.value = value;
    }

    public String getValue(){ return this.value;}
}
