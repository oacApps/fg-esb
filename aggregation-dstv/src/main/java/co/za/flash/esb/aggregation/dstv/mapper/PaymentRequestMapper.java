package co.za.flash.esb.aggregation.dstv.mapper;

import co.za.flash.esb.aggregation.dstv.dto.request.PaymentRequestDTO;
import co.za.flash.esb.aggregation.dstv.model.DSTVRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface PaymentRequestMapper {
    PaymentRequestMapper INSTANCE = Mappers.getMapper(PaymentRequestMapper.class);

    @Mappings({
            /*@Mapping(target = "storeid", expression = "java(Long.parseLong(paymentRequestDTO.getStoreid()))"),
            @Mapping(target = "tillid", expression = "java(Long.parseLong(paymentRequestDTO.getTillid()))"),*/
            @Mapping(target = "req_type", constant = "1L"),
            @Mapping(target = "utility", constant = "7L"),
            @Mapping(target = "network", constant = "63L"),
            @Mapping(target = "trans_pin_type", constant = "0L")
    })
    DSTVRequestMdl toMdl(PaymentRequestDTO paymentRequestDTO) throws ParseException;
}
