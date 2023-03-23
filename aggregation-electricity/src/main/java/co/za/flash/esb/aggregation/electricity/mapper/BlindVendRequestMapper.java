package co.za.flash.esb.aggregation.electricity.mapper;

import co.za.flash.esb.aggregation.electricity.dto.BlindVendRequestDTO;
import co.za.flash.esb.aggregation.electricity.model.request.ElectricityRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface BlindVendRequestMapper {
    BlindVendRequestMapper INSTANCE = Mappers.getMapper(BlindVendRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(null == blindVendRequestDTO.getStoreid() ? null : Long.parseLong(blindVendRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(null == blindVendRequestDTO.getTillid() ? null : Long.parseLong(blindVendRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "0L"),
            @Mapping(target = "network", constant = "0L"),
            @Mapping(target = "trans_pin_type", constant = "1L"),
            @Mapping(target = "prevend", constant = "5L")
    })
    ElectricityRequestMdl toMdl(BlindVendRequestDTO blindVendRequestDTO) throws ParseException;
}
