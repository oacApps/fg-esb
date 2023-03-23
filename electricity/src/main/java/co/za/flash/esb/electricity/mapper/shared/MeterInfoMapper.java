package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.MeterInfoDTO;
import co.za.flash.esb.electricity.model.shared.MeterInfoMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeterInfoMapper {
    MeterInfoMapper INSTANCE = Mappers.getMapper(MeterInfoMapper.class);
    @Mappings({
            @Mapping(source = "meterNumber", target = "meterNumber" , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(source = "supplyGrpCode", target = "supplyGrpCode" , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(source = "keyRevisionNumber", target = "keyRevisionNumber" , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(source = "tokenTechnology", target = "tokenTechnology" , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            @Mapping(source = "algorithmCode", target = "algorithmCode" , nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
            //No TarrifIndex
    })
    /*MeterInfoMdl toMdl(MeterInfoDTO meterInfoDTO);*/


    MeterInfoDTO toDTO(MeterInfoMdl object);


    MeterInfoMdl toMdl(MeterInfoDTO object);
}
