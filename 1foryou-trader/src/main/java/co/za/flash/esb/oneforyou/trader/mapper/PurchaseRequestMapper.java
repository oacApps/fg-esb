package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.trader.model.request.PurchaseRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PurchaseRequestMapper {
    PurchaseRequestMapper INSTANCE = Mappers.getMapper(PurchaseRequestMapper.class);

    @Mappings({
            @Mapping(expression ="java(purchaseRequestDTO.getAccountNumber())", target="user.purseAccountNumber"),
            @Mapping(expression ="java(purchaseRequestDTO.getAmount().getCurrency())", target="currency"),
            @Mapping(expression="java(purchaseRequestDTO.getAmount().getValue())", target="amountRequested"),
            @Mapping(target="device.channelID", constant ="OPEN_API"),
            @Mapping(target="device.appType", constant ="6"),
    })
    PurchaseRequestMdl toMdl(PurchaseRequestDTO purchaseRequestDTO);

}
