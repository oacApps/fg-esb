package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.shared.TokenDTO;
import co.za.flash.esb.oneforyou.trader.model.shared.OneToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TokenMapper {

    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    @Mappings({
            @Mapping(source = "amount", target = "amount.value")
    })
    TokenDTO fromOneToken(OneToken oneToken);
}
