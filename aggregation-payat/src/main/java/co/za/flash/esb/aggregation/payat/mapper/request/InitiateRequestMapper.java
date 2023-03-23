package co.za.flash.esb.aggregation.payat.mapper.request;

import co.za.flash.esb.aggregation.payat.dto.request.InitiateRequestDTO;
import co.za.flash.esb.aggregation.payat.model.request.PayAtReqMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface InitiateRequestMapper {
    InitiateRequestMapper INSTANCE = Mappers.getMapper(InitiateRequestMapper.class);

    @Mappings({
           /* @Mapping(target = "storeid", expression = "java(Long.parseLong(initiateRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(Long.parseLong(initiateRequestDTO.getTillid()))"),*/
            @Mapping(target = "amount", expression = "java(Long.parseLong(initiateRequestDTO.getAmount()))"),
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "7L"),
            @Mapping(target = "network", constant = "54L"),
            @Mapping(target = "trans_pin_type", constant = "0L"),
            @Mapping(target = "srvc_type", constant = "1L"),
    })
    PayAtReqMdl toInitMdl(InitiateRequestDTO initiateRequestDTO) throws ParseException;
}
