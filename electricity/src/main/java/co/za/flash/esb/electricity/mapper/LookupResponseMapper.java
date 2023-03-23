package co.za.flash.esb.electricity.mapper;

import co.za.flash.esb.electricity.dto.response.LookupResponseDTO;
import co.za.flash.esb.electricity.model.response.LookupResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LookupResponseMapper {
    LookupResponseMapper INSTANCE = Mappers.getMapper(LookupResponseMapper.class);
    @Mappings({
            @Mapping(expression="java(null == lookupResponse.getData() ? null : lookupResponse.getData().getRequestId())", target="requestId"),
            @Mapping(source = "message", target = "responseMessage"),
            @Mapping(expression="java(null == lookupResponse.getData() ? null : lookupResponse.getData().getLookupId())", target = "lookupId"),
            @Mapping(expression="java(null == lookupResponse.getData() ? null : lookupResponse.getData().getTransactionDate())", target = "transactionDate"),
            @Mapping(expression="java(null == lookupResponse.getData() ? null : lookupResponse.getData().getAmount())", target = "amount"),
            @Mapping(expression="java(null == lookupResponse.getData() ? null :  co.za.flash.esb.electricity.mapper.shared.BillingInfoMapper.INSTANCE.toDTO(lookupResponse.getData().getBillingInfo()))",
                    target="billingInfo"),
            @Mapping(expression="java(null == lookupResponse.getData() ? null :  co.za.flash.esb.electricity.mapper.shared.MeterInfoMapper.INSTANCE.toDTO(lookupResponse.getData().getMeterInfo()))",
                    target="meterInfo"),
    })
    LookupResponseDTO toDTO(LookupResponseMdl lookupResponse);


}
