package co.za.flash.esb.aggregation.dstv.mapper;

import co.za.flash.esb.aggregation.dstv.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface OneBalanceResponseMapper {
    OneBalanceResponseMapper INSTANCE = Mappers.getMapper(OneBalanceResponseMapper.class);

    OBBillPaymentResponse toOBBillPaymentResponse(OBResponseRefString obResponse) throws ParseException;


    @Mappings({
            @Mapping(source = "OneBalance", target = "oneBalance"),
    })
    OneBalanceSm toOneBalanceSM(OneBalance oneBalance);

    @Mappings({
            @Mapping(source = "OneBalance", target = "oneBalance"),
    })
    OneBalanceRefLongSm toOneBalanceSM(OneBalanceRefLong oneBalance);
}
