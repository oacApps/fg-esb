package co.za.flash.esb.aggregation.dstv.mapper;

import co.za.flash.esb.aggregation.dstv.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface OneBalanceLookupResponseMapper {
    OneBalanceLookupResponseMapper INSTANCE = Mappers.getMapper(OneBalanceLookupResponseMapper.class);

    OBBillPaymentLoopUpResponse toOBBillPaymentResponse(OBResponseRefString obResponse) throws ParseException;


    @Mappings({
            @Mapping(source = "OneBalance", target = "oneBalance"),
    })
    OneBalanceSm toOneBalanceSM(OneBalance oneBalance);
}
