package co.za.flash.esb.aggregation.electricity.mapper;

import co.za.flash.esb.aggregation.electricity.model.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface OneBalanceResponseMapper {
    OneBalanceResponseMapper INSTANCE = Mappers.getMapper(OneBalanceResponseMapper.class);

    OBElecResponse toOBElecResponse(OBResponse obResponse) throws ParseException;
    OBElecCoctResponse toOBElecCoctResponse(OBResponse obResponse) throws ParseException;
    OBElecLookupResponse toOBElecLookupResponse(OBResponse obResponse) throws ParseException;
    OBElecBlindVendResponse toOBElecBlindVendResponse(OBResponse obResponse) throws ParseException;


    @Mappings({
            @Mapping(source = "OneBalance", target = "oneBalance"),
    })
    OneBalanceSm toOneBalanceSM(OneBalance oneBalance);

    @Mappings({
            @Mapping(source = "OneBalance", target = "oneBalance"),
    })
    OneBalanceSm toOneBalanceSM(OneBalanceRefLong oneBalance);
}
