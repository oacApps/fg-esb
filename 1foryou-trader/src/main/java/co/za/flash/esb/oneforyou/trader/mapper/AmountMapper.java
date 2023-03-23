package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.shared.AmountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AmountMapper {

    AmountMapper INSTANCE = Mappers.getMapper(AmountMapper.class);

    @Mappings({
            @Mapping(defaultValue = "ZAR", target = "AmountDTO.currency"),
    })
    AmountDTO map(Integer value);
}

