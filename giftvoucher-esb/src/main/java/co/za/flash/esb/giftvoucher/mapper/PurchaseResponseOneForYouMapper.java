package co.za.flash.esb.giftvoucher.mapper;

import co.za.flash.esb.giftvoucher.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.giftvoucher.dto.response.PurchaseResponseOneForYouDTO;
import java.math.BigInteger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, imports = BigInteger.class)
public interface PurchaseResponseOneForYouMapper {

    PurchaseResponseOneForYouMapper INSTANCE = Mappers.getMapper( PurchaseResponseOneForYouMapper.class );
    @Mappings({
            @Mapping(expression = "java(null == voucherDTO.getPin() ? new BigInteger(\"0\") : new BigInteger(voucherDTO.getPin()))", target = "voucher.pin"),
    })
    PurchaseResponseOneForYouDTO toOneForYouDTO(PurchaseResponseDTO purchaseResponseDTO);
}
