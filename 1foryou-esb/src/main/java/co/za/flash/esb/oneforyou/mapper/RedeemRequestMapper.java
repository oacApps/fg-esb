package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.model.request.RedeemRequestMdl;
import co.za.flash.esb.oneforyou.model.request.trader.RedeemRequestTraderMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RedeemRequestMapper {
    RedeemRequestMapper INSTANCE = Mappers.getMapper(RedeemRequestMapper.class);
    @Mappings({
            @Mapping(source = "requestId", target = "referenceId"),
            @Mapping(source = "voucherPin", target = "pin"),
            @Mapping(source = "amount", target = "amountCents"),
            @Mapping(expression="java(redeemRequest.getAcquirer().getAccount().getAccountNumber())", target = "accountNumber"),

    })
    RedeemRequestMdl toMdl(RedeemRequestDTO redeemRequest);

    @Mappings({
            //No requestId
            //@Mapping(source = "amount", target = "amountRequested"),
            @Mapping(expression="java(redeemRequest.getAcquirer().getAccount().getAccountNumber())", target="accountNumber"),
            @Mapping(source="voucherPin", target="tokenNumber"),
            @Mapping(source="amount", target="amount.value"),
            @Mapping(constant = "ZAR", target="amount.currency"),

    })
    RedeemRequestTraderMdl toTTMdl(RedeemRequestDTO redeemRequest);

}

