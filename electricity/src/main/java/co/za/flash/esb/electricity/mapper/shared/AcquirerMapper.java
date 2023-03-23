package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.AcquirerDTO;
import co.za.flash.esb.electricity.model.shared.AcquirerMdl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AcquirerMapper {
    AcquirerMapper INSTANCE = Mappers.getMapper(AcquirerMapper.class);
    AcquirerDTO toDTO(AcquirerMdl object);
    AcquirerMdl toMdl(AcquirerDTO object);
}
