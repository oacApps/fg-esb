package co.za.flash.ms.georep.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "locations_failed_records")
public class LocationsFailedRecordsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="location_id")
    private int locationId;

    @Column(name="time_stamp")
    private Date timeStamp;

    @Column(name="delete_status")
    private int deleteStatus;
    @Column(name="client_id")
    private  int clientId;
    @Column(name="location_name")
    private String locationName;
    @Column(name="street_address")
    private String streetAddress;
    @Column(name="suburb")
    private String suburb;
    @Column(name="city")
    private String city;
    @Column(name="state")
    private String state;
    @Column(name="country")
    private String country;
    @Column(name="pincode")
    private String pincode;
    @Column(name="location_type")
    private String locationType;

    /* Ignore
    @Column(name="group")
    private String group;*/
    @Column(name="group_split")
    private String groupSplit;
    @Column(name="region")
    private String region;
    @Column(name="contact_person")
    private String contactPerson;
    @Column(name="contact_email")
    private String contactEmail;
    @Column(name="contact_number")
    private String contactNumber;
    @Column(name="user")
    private String user;
    @Column(name="location_user_id")
    private String locationUserId;
    @Column(name="location_user_name")
    private String locationUserName;
    @Column(name="unique_code")
    private String uniqueCode;
    @Column(name="location_code")
    private String locationCode;
    @Column(name="Lat")
    private String lat;
    @Column(name="Lan")
    private String lan;
    @Column(name="status")
    private String status;
    @Column(name="creation_type")
    private String creationType;

    @Column(name="added_on")
    private Date addedOn;

    @Column(name="add_location_id")
    private String addLocationId;
    @Column(name="onf_trader")
    private String onfTrader;
    @Column(name="last_activity")
    private String lastActivity;
    @Column(name="MSISDN")
    private String msisdn;
    @Column(name="IMEI")
    private String imei;
    @Column(name="device_type")
    private String deviceType;
    @Column(name="trader_name")
    private String traderName;
    @Column(name="asylum_permit_number")
    private String asylumPermitNumber;
    @Column(name="trader_id_number")
    private String traderIdNumber;
    @Column(name="trader_cell_number")
    private String traderCellNumber;
    @Column(name="trader_alternate_number")
    private String traderAlternateNumber;
    @Column(name="trader_nationality")
    private String traderNationality;
    @Column(name="trader_language_home")
    private String traderLanguageHome;
    @Column(name="trader_language_preferred")
    private String traderLanguagePreferred;
    @Column(name="contact_method_preferred")
    private String contactMethodPreferred;
    @Column(name="assistant_name")
    private String assistantName;
    @Column(name="assistant_contact_number")
    private String assistantContactNumber;
    @Column(name="pwf_trader")
    private String pwfTrader;
    @Column(name="pwf_account_number")
    private String pwfAccountNumber;
    @Column(name="trader_email")
    private String traderEmail;
    @Column(name="trader_whatsapp_number")
    private String traderWhatsappNumber;
    @Column(name="type_of_id")
    private String typeOfId;
    @Column(name="type_of_id_number")
    private String typeOfIdNumber;
    @Column(name="competitor_device")
    private String competitorDevice;
    @Column(name="supplier")
    private String supplier;
    @Column(name="geo_method")
    private String geoMethod;
    @Column(name="tap2pay")
    private String tap2pay;
    @Column(name="visited")
    private String visited;
    @Column(name="geo_loc_updated")
    private String geoLocUpdated;
}
