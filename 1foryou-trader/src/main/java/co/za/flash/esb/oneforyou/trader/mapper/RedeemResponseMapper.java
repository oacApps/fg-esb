package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.model.response.RedeemResponseMdl;
import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseDTO;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
uses = ChangeVoucherMapper.class)
public interface RedeemResponseMapper {
    RedeemResponseMapper INSTANCE = Mappers.getMapper(RedeemResponseMapper.class);

    @Mappings({
            @Mapping(source ="actionCode", target="responseCode"),
            @Mapping(source ="screenMessage", target="responseMessage"),
            @Mapping(source ="transactionReference", target="transactionId"),
            @Mapping(constant = "ZAR", target="amount.currency"),
            @Mapping(source = "amountAuthorised", target="amount.value"),
            @Mapping(constant = "ZAR", target="balance.currency"),
            @Mapping(source = "balance", target="balance.value"),
            @Mapping(constant = "ZAR", target="availableBalance.currency"),
            @Mapping(source = "availableBalance", target="availableBalance.value"),
    })
    RedeemResponseDTO toDTO(RedeemResponseMdl redeemResponseMdl);

    @Mappings({
            @Mapping(expression="java(String.valueOf(amountDTO.getValue()))", target="amount.value"),
            @Mapping(expression = "java(String.valueOf(balanceDTO.getValue()))", target="balance.value"),
            @Mapping(expression = "java(String.valueOf(availableBalanceDTO.getValue()))", target="availableBalance.value"),
            @Mapping(expression = "java(String.valueOf(redeemResponseDTO.getSequenceNumber()))", target="sequenceNumber"),
    })
    RedeemResponseMatchWsoDTO toWso2DTO(RedeemResponseDTO redeemResponseDTO);

}
