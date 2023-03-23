package co.za.flash.esb.aggregation.payat.mapper.response;

import co.za.flash.esb.aggregation.payat.dto.response.CompleteResponseDTO;
import co.za.flash.esb.aggregation.payat.model.response.CompleteResponseMdl;
import co.za.flash.esb.aggregation.payat.model.response.OBBillPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface CompleteResponseMapper {
    CompleteResponseMapper INSTANCE = Mappers.getMapper(CompleteResponseMapper.class);
    @Mapping(target = "_version", source = "version")
    CompleteResponseDTO toDTO(OBBillPaymentResponse OBBillPaymentResponse) throws ParseException;
}
