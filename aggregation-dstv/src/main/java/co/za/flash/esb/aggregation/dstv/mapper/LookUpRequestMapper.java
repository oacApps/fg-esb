package co.za.flash.esb.aggregation.dstv.mapper;

import co.za.flash.esb.aggregation.dstv.dto.request.LookUpRequestDTO;
import co.za.flash.esb.aggregation.dstv.model.DSTVRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface LookUpRequestMapper {
    LookUpRequestMapper INSTANCE = Mappers.getMapper(LookUpRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(Long.parseLong(lookUpRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(Long.parseLong(lookUpRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "7L"),
            @Mapping(target = "network", constant = "63L"),
            @Mapping(target = "trans_pin_type", constant = "0L")
    })
    DSTVRequestMdl toLookUpMdl(LookUpRequestDTO lookUpRequestDTO) throws ParseException;
}
