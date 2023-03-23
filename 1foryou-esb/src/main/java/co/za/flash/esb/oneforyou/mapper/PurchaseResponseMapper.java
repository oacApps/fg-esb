package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.oneforyou.model.response.PurchaseResponseMdl;
import co.za.flash.esb.oneforyou.model.response.trader.PurchaseResponseTraderMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PurchaseResponseMapper {
    PurchaseResponseMapper INSTANCE = Mappers.getMapper(PurchaseResponseMapper.class);

    @Mappings({
            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getTransaction().getReference())", target="requestId"),
            @Mapping(source = "message", target = "responseMessage"),
            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? null : String.valueOf(purchaseResponseMdl.getData().getToken().getObTransactionId()))", target="transactionId"),
            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getTransaction().getCreated())", target="transactionDate"),

            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getToken().getPin())", target="voucher.pin"),
            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getToken().getSerialNumber())", target="voucher.serialNumber"),
            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? null : purchaseResponseMdl.getData().getToken().getExpiryDate())", target="voucher.expiryDate"),
            @Mapping(expression="java(null == purchaseResponseMdl.getData() ? 0 : purchaseResponseMdl.getData().getToken().getAmountCents())", target="voucher.amount"),
    })
    PurchaseResponseDTO toDTO(PurchaseResponseMdl purchaseResponseMdl);


    @Mappings({
            @Mapping(expression="java(null == purchaseResponseTraderMdl.getToken() ? null : purchaseResponseTraderMdl.getToken().getPin())", target="voucher.pin"),
            @Mapping(expression="java(null == purchaseResponseTraderMdl.getToken() ? null : purchaseResponseTraderMdl.getToken().getSerialNumber())", target="voucher.serialNumber"),
            @Mapping(expression="java(null == purchaseResponseTraderMdl.getToken() ? null : purchaseResponseTraderMdl.getToken().getExpiryDate())", target="voucher.expiryDate"),
            @Mapping(expression="java(null == purchaseResponseTraderMdl.getToken() ? 0 : purchaseResponseTraderMdl.getToken().getAmount())", target="voucher.amount"),
            @Mapping(expression="java(null == purchaseResponseTraderMdl.getBalance() ? null : Long.valueOf(purchaseResponseTraderMdl.getBalance().getValue()))", target="acquirer.account.balance"),
            @Mapping(expression="java(null == purchaseResponseTraderMdl.getAvailableBalance() ? null : Long.valueOf(purchaseResponseTraderMdl.getAvailableBalance().getValue()))", target="acquirer.account.availableBalance"),

    })
    PurchaseResponseDTO toDTOFromTrader(PurchaseResponseTraderMdl purchaseResponseTraderMdl);
}
