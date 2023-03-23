package co.za.flash.esb.aggregation.electricity.mapper;

import co.za.flash.esb.aggregation.electricity.dto.LookupRequestDTO;
import co.za.flash.esb.aggregation.electricity.model.request.ElectricityRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface LookUpRequestMapper {
    LookUpRequestMapper INSTANCE = Mappers.getMapper(LookUpRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(null == lookupRequestDTO.getStoreid() ? null : Long.parseLong(lookupRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(null == lookupRequestDTO.getTillid() ? null : Long.parseLong(lookupRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "0L"),
            @Mapping(target = "network", constant = "44L"),
            @Mapping(target = "trans_pin_type", constant = "1L"),
            @Mapping(target = "prevend", constant = "1L")
    })
    ElectricityRequestMdl toMdl(LookupRequestDTO lookupRequestDTO) throws ParseException;
}
