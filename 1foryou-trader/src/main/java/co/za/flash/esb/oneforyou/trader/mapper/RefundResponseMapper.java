package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RedeemResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RefundRedemptionResponseDTO;
import co.za.flash.esb.oneforyou.trader.dto.response.RefundRedemptionResponseMatchWsoDTO;
import co.za.flash.esb.oneforyou.trader.model.response.RefundResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RefundResponseMapper {

    RefundResponseMapper INSTANCE = Mappers.getMapper(RefundResponseMapper.class);

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
            @Mapping(target="changeVoucher.pin", source = "refundedVoucher.pin"),
            @Mapping(target="changeVoucher.serialNumber", source = "refundedVoucher.serialNumber"),
            @Mapping(target="changeVoucher.expiryDate", source = "refundedVoucher.expiryDate"),
            @Mapping(target="changeVoucher.status", source = "refundedVoucher.status"),
            @Mapping(target="changeVoucher.amount.currency", constant = "ZAR"),
            @Mapping(target="changeVoucher.amount.value", source = "refundedVoucher.amount"),
    })
    RefundRedemptionResponseDTO toDTO(RefundResponseMdl refundResponseMdl);

    @Mappings({
            @Mapping(expression = "java(null)", target="changeVoucher.amount.value"),
    })
    RefundRedemptionResponseMatchWsoDTO toWso2DTO(RefundRedemptionResponseDTO refundRedemptionResponseDTO);
}

