package co.za.flash.esb.cellular.mapper;

import co.za.flash.esb.cellular.model.OneBalance;
import co.za.flash.esb.cellular.model.OneBalanceSm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OneBalanceResponseMapper {
    OneBalanceResponseMapper INSTANCE = Mappers.getMapper( OneBalanceResponseMapper.class );

    @Mappings({
            @Mapping(source = "OneBalance", target = "oneBalance"),
    })
    OneBalanceSm toOneBalanceSM(OneBalance oneBalance);
}
