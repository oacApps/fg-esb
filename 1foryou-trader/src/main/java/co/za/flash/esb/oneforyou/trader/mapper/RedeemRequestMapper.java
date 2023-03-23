package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.trader.model.request.RedeemRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RedeemRequestMapper {
    RedeemRequestMapper INSTANCE = Mappers.getMapper(RedeemRequestMapper.class);

    @Mappings({
            @Mapping(expression ="java(redeemRequestDTO.getAccountNumber())", target="user.purseAccountNumber"),
            @Mapping(source ="tokenNumber", target="voucherPin"),
            @Mapping(expression ="java(redeemRequestDTO.getAmount().getCurrency())", target="currency"),
            @Mapping(expression="java(redeemRequestDTO.getAmount().getValue())", target="amountRequested"),
            @Mapping(target="device.channelID", constant ="OPEN_API"),
            @Mapping(target="device.appType", constant ="6"),
    })
    RedeemRequestMdl toMdl(RedeemRequestDTO redeemRequestDTO);

}
