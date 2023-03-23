package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.request.RefundRedemptionRequestDTO;
import co.za.flash.esb.oneforyou.trader.model.request.RefundRedemptionRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RefundRequestMapper {

    RefundRequestMapper INSTANCE = Mappers.getMapper(RefundRequestMapper.class);

    @Mappings({
            @Mapping(expression ="java(refundRedemptionRequestDTO.getAccountNumber())", target="user.purseAccountNumber"),
            @Mapping(expression ="java(refundRedemptionRequestDTO.getAmount().getCurrency())", target="currency"),
            @Mapping(expression="java(refundRedemptionRequestDTO.getAmount().getValue())", target="amountRequested"),
            @Mapping(target="device.channelID", constant ="OPEN_API"),
            @Mapping(target="device.appType", constant ="6"),
            @Mapping(source = "redemptionTransactionId", target = "redemptionReference"),
    })
    RefundRedemptionRequestMdl toMdl(RefundRedemptionRequestDTO refundRedemptionRequestDTO);
}
