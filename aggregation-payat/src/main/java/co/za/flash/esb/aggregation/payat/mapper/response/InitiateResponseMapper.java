package co.za.flash.esb.aggregation.payat.mapper.response;

import co.za.flash.esb.aggregation.payat.dto.response.InitiateResponseDTO;
import co.za.flash.esb.aggregation.payat.model.response.InitiateResponseMdl;
import co.za.flash.esb.aggregation.payat.model.response.OBBillPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface InitiateResponseMapper {
    InitiateResponseMapper INSTANCE = Mappers.getMapper(InitiateResponseMapper.class);
    @Mapping(target = "_version", source = "version")
    InitiateResponseDTO toDTO(OBBillPaymentResponse OBBillPaymentResponse) throws ParseException;
}
