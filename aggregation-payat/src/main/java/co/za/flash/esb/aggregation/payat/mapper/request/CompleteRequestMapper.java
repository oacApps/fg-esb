package co.za.flash.esb.aggregation.payat.mapper.request;

import co.za.flash.esb.aggregation.payat.dto.request.CommonRequestDTO;
import co.za.flash.esb.aggregation.payat.dto.request.CompleteRequestDTO;
import co.za.flash.esb.aggregation.payat.model.request.PayAtReqMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface CompleteRequestMapper {
    CompleteRequestMapper INSTANCE = Mappers.getMapper(CompleteRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(Long.parseLong(completeRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(Long.parseLong(completeRequestDTO.getTillid()))"),*/
            @Mapping(target = "amount", expression = "java(Long.parseLong(completeRequestDTO.getAmount()))"),
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "7L"),
            @Mapping(target = "network", constant = "54L"),
            @Mapping(target = "trans_pin_type", constant = "0L"),
            @Mapping(target = "srvc_type", constant = "2L"),
    })
    PayAtReqMdl toCommMdl(CompleteRequestDTO completeRequestDTO) throws ParseException;
}
