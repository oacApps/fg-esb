package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.UtilityInfoDTO;
import co.za.flash.esb.electricity.model.shared.UtilityInfoMdl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UtilityInfoMapper {

    UtilityInfoMapper INSTANCE = Mappers.getMapper(UtilityInfoMapper.class);

    UtilityInfoDTO toDTO(UtilityInfoMdl utilityInfo);
}
