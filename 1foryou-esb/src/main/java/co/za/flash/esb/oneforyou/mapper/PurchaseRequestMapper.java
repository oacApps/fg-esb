package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.model.request.PurchaseRequestMdl;
import co.za.flash.esb.oneforyou.model.request.trader.PurchaseRequestTraderMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PurchaseRequestMapper {
    PurchaseRequestMapper INSTANCE = Mappers.getMapper(PurchaseRequestMapper.class);

    @Mappings({
            @Mapping(expression="java(purchaseRequest.getAcquirer().getAccount().getAccountNumber())", target="accountNumber"),
            @Mapping(source = "requestId", target = "referenceId"),
            @Mapping(source = "amount", target = "amountCents"),
    })
    PurchaseRequestMdl toMdl(PurchaseRequestDTO purchaseRequest);


    @Mappings({
            //No requestId
            //@Mapping(source = "amount", target = "amountRequested"),
            @Mapping(expression="java(purchaseRequest.getAcquirer().getAccount().getAccountNumber())", target="accountNumber"),
            @Mapping(source="amount", target="amount.value"),
            @Mapping(constant = "ZAR", target="amount.currency"),

    })
    PurchaseRequestTraderMdl toTTMdl(PurchaseRequestDTO purchaseRequest);

}
