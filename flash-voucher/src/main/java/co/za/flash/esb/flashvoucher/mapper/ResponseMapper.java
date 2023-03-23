package co.za.flash.esb.flashvoucher.mapper;

import co.za.flash.esb.flashvoucher.dto.RequestDTO;
import co.za.flash.esb.flashvoucher.dto.ResponseDTO;
import co.za.flash.esb.flashvoucher.dto.TokenDTO;
import co.za.flash.esb.flashvoucher.model.CashVoucherMDL;
import co.za.flash.esb.flashvoucher.model.RequestMDL;
import co.za.flash.esb.flashvoucher.model.ResponseMDL;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ResponseMapper {

    ResponseMapper INSTANCE = Mappers.getMapper(ResponseMapper.class);
    //target = "acquirer.account.balance"
    @Mappings({
            @Mapping(source = "actionCode", target = "responseCode"),
            @Mapping(source="screenMessage", target = "receipt"),
            @Mapping(source="transactionReference", target = "transactionID"),
    })
    ResponseDTO toDTO (ResponseMDL responseMDL);

    @Mappings({
            @Mapping(source = "currencyCode", target = "amount.currency"),
            @Mapping(source="amount", target = "amount.value"),
    })
   TokenDTO toDTO(CashVoucherMDL cashVoucherMDL);
}
