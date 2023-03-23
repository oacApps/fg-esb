package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.CustomerInfo;
import co.za.flash.esb.electricity.model.shared.ConsumerInfoMdl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ConsumerInfoMapper {
    ConsumerInfoMapper INSTANCE = Mappers.getMapper(ConsumerInfoMapper.class);

    CustomerInfo toDTO(ConsumerInfoMdl consumerInfo);
    //ConsumerInfoMdl toMdl(CustomerInfo consumerInfo);
}


