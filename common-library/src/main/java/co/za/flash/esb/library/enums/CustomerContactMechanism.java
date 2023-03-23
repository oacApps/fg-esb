package co.za.flash.esb.library.enums;

public enum CustomerContactMechanism {
    SMS ("SMS"),
    EMAIL ("EMAIL");

    private final String value;

    CustomerContactMechanism(String value) {
        this.value = value;
    }

    public String getValue(){ return this.value;}
}

