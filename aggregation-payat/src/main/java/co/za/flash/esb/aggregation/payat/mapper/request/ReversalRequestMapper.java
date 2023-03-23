package co.za.flash.esb.aggregation.payat.mapper.request;

import co.za.flash.esb.aggregation.payat.dto.request.ReversalRequestDTO;
import co.za.flash.esb.aggregation.payat.dto.response.ReversalResponseDTO;
import co.za.flash.esb.aggregation.payat.model.request.PayAtReqMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface ReversalRequestMapper {
    ReversalRequestMapper INSTANCE = Mappers.getMapper(ReversalRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(Long.parseLong(reversalRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(Long.parseLong(reversalRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "7L"),
            @Mapping(target = "network", constant = "54L"),
            @Mapping(target = "trans_pin_type", constant = "0L"),
            @Mapping(target = "srvc_type", constant = "2L"),
    })
    PayAtReqMdl toRevMdl(ReversalRequestDTO reversalRequestDTO) throws ParseException;
}
