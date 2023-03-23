package co.za.flash.esb.electricity.mapper;

import co.za.flash.esb.electricity.dto.request.LookupRequestDTO;
import co.za.flash.esb.electricity.model.request.LookupRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LookupRequestMapper {
    LookupRequestMapper INSTANCE = Mappers.getMapper(LookupRequestMapper.class);
    LookupRequestMdl toMdl(LookupRequestDTO object);
}
