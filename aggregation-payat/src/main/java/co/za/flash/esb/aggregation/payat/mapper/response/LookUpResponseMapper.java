package co.za.flash.esb.aggregation.payat.mapper.response;

import co.za.flash.esb.aggregation.payat.dto.response.LookUpResponseDTO;
import co.za.flash.esb.aggregation.payat.model.response.LookUpResponseMdl;
import co.za.flash.esb.aggregation.payat.model.response.OBBillPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface LookUpResponseMapper {
    LookUpResponseMapper INSTANCE = Mappers.getMapper(LookUpResponseMapper.class);

    @Mapping(target = "_version", source = "version")
    LookUpResponseDTO toDTO(OBBillPaymentResponse OBBillPaymentResponse) throws ParseException;
}
