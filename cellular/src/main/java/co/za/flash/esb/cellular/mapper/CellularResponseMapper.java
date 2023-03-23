package co.za.flash.esb.cellular.mapper;

import co.za.flash.esb.cellular.dto.CellularPinLessResponseDTO;
import co.za.flash.esb.cellular.model.CellularResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.text.ParseException;

@Mapper
public interface CellularResponseMapper {
    CellularResponseMapper INSTANCE = Mappers.getMapper( CellularResponseMapper.class );
    @Mappings({
            @Mapping(target = "responseCode", expression = "java(cellularResponseMdl.OBAirtimeResponse.getResponseCode())"),
/*
            @Mapping(target = "referenceId", expression = "java(cellularResponseMdl.OBAirtimeResponse.getReferenceId())"),
*/
            @Mapping(target = "callCentre", expression = "java(Long.parseLong(cellularResponseMdl.OBAirtimeResponse.getCallCentre()))"),
            @Mapping(target = "rechargeResponseType", expression = "java(cellularResponseMdl.OBAirtimeResponse.getRechargeResponseType())"),
            @Mapping(target = "responseMessage", expression = "java(cellularResponseMdl.OBAirtimeResponse.getResponseMessage())"),
            @Mapping(target = "_version", expression = "java(cellularResponseMdl.OBAirtimeResponse.getVersion())"),
            @Mapping(target = "transactionID", expression = "java(cellularResponseMdl.OBAirtimeResponse.getTransactionID())")
    })
    CellularPinLessResponseDTO toDTO(CellularResponseMdl cellularResponseMdl)  throws ParseException;
}
