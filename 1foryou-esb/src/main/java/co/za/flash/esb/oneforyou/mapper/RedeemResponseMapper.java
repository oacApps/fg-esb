package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.response.RedeemResponseDTO;
import co.za.flash.esb.oneforyou.dto.response.RedeemResponseTraderDTO;
import co.za.flash.esb.oneforyou.model.response.RedeemResponseMdl;
import co.za.flash.esb.oneforyou.model.response.trader.RedeemResponseTraderMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RedeemResponseMapper {
    RedeemResponseMapper INSTANCE = Mappers.getMapper(RedeemResponseMapper.class);

    @Mappings({
            @Mapping(expression = "java(null == redeemResponseMdl.getData() ? null : String.valueOf(redeemResponseMdl.getData().getSwitchId()))", target = "transactionId"),
            @Mapping(expression = "java(null == redeemResponseMdl.getData() ? null : redeemResponseMdl.getData().getTransaction().getCreated())", target = "transactionDate"),
            @Mapping(expression = "java(null == redeemResponseMdl.getData() ? 0 : redeemResponseMdl.getData().getAmountCents())", target = "amount"),
            @Mapping(expression = "java(null == redeemResponseMdl.getData() ? null : redeemResponseMdl.getData().getTransaction().getReference())", target = "requestId"),
    })
    RedeemResponseDTO toDTO(RedeemResponseMdl redeemResponseMdl);

    @Mappings({
            @Mapping(source = "redeemResponseTraderMdl.changeVoucher.amount.value", target = "changeVoucher.amount"),
            @Mapping(expression = "java(null != redeemResponseTraderMdl.getAmount() && redeemResponseTraderMdl.getAmount().getValue().equalsIgnoreCase(\"null\") ? 0L : Long.valueOf(redeemResponseTraderMdl.getAmount().getValue()))", target = "amount"),
            @Mapping(expression = "java(null != redeemResponseTraderMdl.getBalance() && redeemResponseTraderMdl.getBalance().getValue().equalsIgnoreCase(\"null\") ? 0L : Long.valueOf(redeemResponseTraderMdl.getBalance().getValue()))", target = "acquirer.account.balance"),
            @Mapping(expression = "java(null != redeemResponseTraderMdl.getAvailableBalance() && redeemResponseTraderMdl.getAvailableBalance().getValue().equalsIgnoreCase(\"null\") ? 0L : Long.valueOf(redeemResponseTraderMdl.getAvailableBalance().getValue()))", target = "acquirer.account.availableBalance"),
    })
    RedeemResponseTraderDTO toDTOFromTrader(RedeemResponseTraderMdl redeemResponseTraderMdl);
}
