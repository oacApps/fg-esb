package co.za.flash.esb.giftvoucher.mapper;

import co.za.flash.esb.giftvoucher.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.giftvoucher.model.PurchaseResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.math.BigInteger;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, imports = BigInteger.class)
public interface PurchaseResponseMapper {

    PurchaseResponseMapper INSTANCE = Mappers.getMapper( PurchaseResponseMapper.class );
    // APIM request mapping to what is expected by BA request
    @Mappings({
            @Mapping(source = "message", target = "responseMessage"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getToken().getObTransactionId())", target = "transactionID"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getTransaction().getCreated())", target = "responseTime"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getToken().getPin())", target = "voucher.pin"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? new BigInteger(\"0\") : new BigInteger(purchaseResponseMdl.getData().getToken().getSerialNumber()))", target = "voucher.serialNumber"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? \"null\" : purchaseResponseMdl.getData().getToken().getExpiryDate())", target = "voucher.expiryDate"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? 0 : purchaseResponseMdl.getData().getToken().getAmountCents())", target = "voucher.amount"),
            @Mapping(expression = "java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getToken().getReferenceId())", target = "referenceId"),
    })
    PurchaseResponseDTO toDTO(PurchaseResponseMdl purchaseResponseMdl);
}
