package co.za.flash.esb.giftvoucher.mapper;

import co.za.flash.esb.giftvoucher.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.giftvoucher.model.PurchaseRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PurchaseRequestMapper {

    PurchaseRequestMapper INSTANCE = Mappers.getMapper(PurchaseRequestMapper.class);
    // APIM request mapping to what is expected by BA request
    @Mappings({
            @Mapping(source = "amount", target = "amountCents"),
            @Mapping(source = "voucherType", target = "vendor"),
            //@Mapping(target = "vendor",expression = "java(purchaseRequestDTO.getVoucherType().getType())")
    })
    PurchaseRequestMdl toMdl(PurchaseRequestDTO purchaseRequestDTO);
}


