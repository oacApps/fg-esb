package co.za.flash.esb.oneforyou.mapper;

import co.za.flash.esb.oneforyou.dto.response.RefundResponseDTO;
import co.za.flash.esb.oneforyou.model.response.RefundResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RefundResponseMapper {
    RefundResponseMapper INSTANCE = Mappers.getMapper(RefundResponseMapper.class);

    @Mappings({
            @Mapping(expression="java(null == refundResponseMdl.getData() ? null : refundResponseMdl.getData().getRequestId())", target="requestId"),
            @Mapping(expression="java(null == refundResponseMdl.getData() ? null : refundResponseMdl.getData().getRedemptionRequestId())", target="transactionId"),
            @Mapping(expression="java(null == refundResponseMdl.getData() ? null : refundResponseMdl.getData().getTransactionDate())", target="transactionDate"),

            @Mapping(expression="java(null == refundResponseMdl.getData() ? null : refundResponseMdl.getData().getVoucherPin())", target="voucher.pin"),
            @Mapping(expression="java(null == refundResponseMdl.getData() ? null : refundResponseMdl.getData().getVoucherSerial())", target="voucher.serialNumber"),
            @Mapping(expression="java(null == refundResponseMdl.getData() ? null : refundResponseMdl.getData().getExpiryDate())", target="voucher.expiryDate"),
            @Mapping(expression="java(null == refundResponseMdl.getData() ? 0 : refundResponseMdl.getData().getAmount())", target="voucher.amount"),
    })
    RefundResponseDTO toDTO(RefundResponseMdl refundResponseMdl);
}
