package co.za.flash.esb.aggregation.payat.mapper.response;

import co.za.flash.esb.aggregation.payat.dto.response.ReversalResponseDTO;
import co.za.flash.esb.aggregation.payat.model.response.OBBillPaymentResponse;
import co.za.flash.esb.aggregation.payat.model.response.ReversalResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface ReversalResponseMapper {
    ReversalResponseMapper INSTANCE = Mappers.getMapper(ReversalResponseMapper.class);
    @Mapping(target = "_version", source = "version")
    ReversalResponseDTO toDTO(OBBillPaymentResponse OBBillPaymentResponse) throws ParseException;
}
