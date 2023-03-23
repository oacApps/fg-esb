package co.za.flash.esb.cellular.mapper;

import co.za.flash.esb.cellular.dto.CellularRequestDTO;
import co.za.flash.esb.cellular.model.CellularRequestMdl;
import co.za.flash.esb.library.enums.cellular.CellularNetwork;
import co.za.flash.esb.library.enums.cellular.CellularProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper(componentModel = "spring",  imports = {CellularNetwork.class, CellularProductType.class})
public interface CellularRequestMapper {

    CellularRequestMapper INSTANCE = Mappers.getMapper(CellularRequestMapper.class);

    @Mappings({
            @Mapping(source = "referenceId", target = "reference_id"),
            @Mapping(target = "network", expression = "java(CellularNetwork.fromString(cellularRequestDTO.getNetwork()).getSerialNumber())"),
            @Mapping(target = "voucher_type", expression = "java((long)cellularRequestDTO.getType().getValue())"),
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "1L")
                    })
    CellularRequestMdl toMdl(CellularRequestDTO cellularRequestDTO) throws ParseException;

}
