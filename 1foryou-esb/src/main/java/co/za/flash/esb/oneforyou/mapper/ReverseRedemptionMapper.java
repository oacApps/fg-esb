package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.request.ReverseRedemptionDTO;
import co.za.flash.esb.oneforyou.dto.response.ReverseResponseTraderDTO;
import co.za.flash.esb.oneforyou.model.request.trader.RefundRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.request.trader.ReverseRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.ReversalsRedemptionResponseTraderMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReverseRedemptionMapper {

    ReverseRedemptionMapper INSTANCE = Mappers.getMapper(ReverseRedemptionMapper.class);

    @Mappings({
            @Mapping(expression="java(reverseRedemption.getAcquirer().getAccount().getAccountNumber())", target="accountNumber"),
            @Mapping(source="amount", target="amount.value"),
            @Mapping(constant = "ZAR", target="amount.currency"),
    })
    ReverseRedemptionRequestTraderMdl toMdl(ReverseRedemptionDTO reverseRedemption);


    @Mappings({
            @Mapping(expression="java(Long.valueOf(reversalsRedemptionResponseTraderMdl.getBalance().getValue()))", target="acquirer.account.balance"),
            @Mapping(expression="java(Long.valueOf(reversalsRedemptionResponseTraderMdl.getAvailableBalance().getValue()))", target="acquirer.account.availableBalance"),
    })
    ReverseResponseTraderDTO toDTO(ReversalsRedemptionResponseTraderMdl reversalsRedemptionResponseTraderMdl);
}
