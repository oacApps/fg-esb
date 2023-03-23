package co.za.flash.esb.cellular.mapper;

import co.za.flash.esb.cellular.model.OBAirtimeResponse;
import co.za.flash.esb.cellular.model.OBAirtimeResponseSm;
import co.za.flash.esb.cellular.model.OBAirtimeResponseTelkom;
import co.za.flash.esb.cellular.model.OBAirtimeResponseTelkomSm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface OBAirtimeResponseMapper {
    OBAirtimeResponseMapper INSTANCE = Mappers.getMapper( OBAirtimeResponseMapper.class );

    @Mappings({
            @Mapping(source = "OBAirtimeResponse", target = "obairtimeResponse"),
    })
    OBAirtimeResponseSm toObAirtimeSm(OBAirtimeResponse obAirtimeResponse);


    @Mappings({
            @Mapping(source = "OBAirtimeResponse", target = "obairtimeResponse"),
    })
    OBAirtimeResponseTelkomSm toObAirtimeTelkomSm(OBAirtimeResponseTelkom obAirtimeResponseTelkom);
}
