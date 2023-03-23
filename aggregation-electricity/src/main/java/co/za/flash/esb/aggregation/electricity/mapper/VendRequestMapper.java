package co.za.flash.esb.aggregation.electricity.mapper;

import co.za.flash.esb.aggregation.electricity.dto.LookupRequestDTO;
import co.za.flash.esb.aggregation.electricity.dto.common.ElectricityRequestDTO;
import co.za.flash.esb.aggregation.electricity.model.request.ElectricityRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface VendRequestMapper {
    VendRequestMapper INSTANCE = Mappers.getMapper(VendRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(null == electricityRequestDTO.getStoreid() ? null : Long.parseLong(electricityRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(null == electricityRequestDTO.getTillid() ? null : Long.parseLong(electricityRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "0L"),
            @Mapping(target = "network", constant = "44L"),
            @Mapping(target = "trans_pin_type", constant = "1L"),
            @Mapping(target = "prevend", constant = "0L")
    })
    ElectricityRequestMdl toMdl(ElectricityRequestDTO electricityRequestDTO) throws ParseException;
}
