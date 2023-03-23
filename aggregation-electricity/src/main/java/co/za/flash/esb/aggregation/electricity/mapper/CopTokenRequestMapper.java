package co.za.flash.esb.aggregation.electricity.mapper;

import co.za.flash.esb.aggregation.electricity.dto.CopyTokenRequestDTO;
import co.za.flash.esb.aggregation.electricity.dto.LookupRequestDTO;
import co.za.flash.esb.aggregation.electricity.model.request.ElectricityRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface CopTokenRequestMapper {
    CopTokenRequestMapper INSTANCE = Mappers.getMapper(CopTokenRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(null == copyTokenRequestDTO.getStoreid() ? null : Long.parseLong(copyTokenRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(null == copyTokenRequestDTO.getTillid() ? null : Long.parseLong(copyTokenRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "0L"),
            @Mapping(target = "network", constant = "5L"),
            @Mapping(target = "trans_pin_type", constant = "1L"),
            @Mapping(target = "prevend", constant = "2L")
    })
    ElectricityRequestMdl toMdl(CopyTokenRequestDTO copyTokenRequestDTO) throws ParseException;
}
