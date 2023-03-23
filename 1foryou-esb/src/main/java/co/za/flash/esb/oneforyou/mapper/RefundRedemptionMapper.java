package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.request.RefundRedemptionDTO;
import co.za.flash.esb.oneforyou.dto.response.RefundResponseDTO;
import co.za.flash.esb.oneforyou.dto.response.RefundResponseTraderDTO;
import co.za.flash.esb.oneforyou.model.request.trader.RefundRedemptionRequestTraderMdl;
import co.za.flash.esb.oneforyou.model.response.trader.RefundRedemptionResponseTraderMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RefundRedemptionMapper {

    RefundRedemptionMapper INSTANCE = Mappers.getMapper(RefundRedemptionMapper.class);

    @Mappings({
            @Mapping(expression="java(refundRedemption.getAcquirer().getAccount().getAccountNumber())", target="accountNumber"),
            @Mapping(source="amount", target="amount.value"),
            @Mapping(constant = "ZAR", target="amount.currency"),
    })
    RefundRedemptionRequestTraderMdl toMdl(RefundRedemptionDTO refundRedemption);

    @Mappings({
            @Mapping(source = "changeVoucher.pin", target="voucher.pin"),
            @Mapping(source = "changeVoucher.serialNumber", target="voucher.serialNumber"),
            @Mapping(source = "changeVoucher.expiryDate", target="voucher.expiryDate"),
            @Mapping(source = "changeVoucher.amount.value", target="voucher.amount"),
    })
    RefundResponseDTO toDTO(RefundRedemptionResponseTraderMdl refundRedemptionResponseTraderMdl);

    RefundResponseTraderDTO toTraderDTO(RefundResponseDTO refundResponseDTO);
}
