package co.za.flash.esb.electricity.mapper;

import co.za.flash.esb.electricity.dto.response.PurchaseResponseDTO;
import co.za.flash.esb.electricity.model.response.PurchaseResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PurchaseResponseMapper {
   PurchaseResponseMapper INSTANCE = Mappers.getMapper(PurchaseResponseMapper.class)  ;
   @Mappings({
           @Mapping(expression="java(null == purchaseResponse.getData() ? null : purchaseResponse.getData().getRequestId())",
                   target="requestId"),
           @Mapping(source = "message", target = "responseMessage"),
           @Mapping(expression="java(null == purchaseResponse.getData() ? null : purchaseResponse.getData().getTransactionId())",
                   target="transactionId"),
           @Mapping(expression="java(null == purchaseResponse.getData() ? null : purchaseResponse.getData().getTransactionDate())",
                   target="transactionDate"),
           @Mapping(expression="java(null == purchaseResponse.getData() ? 0 : purchaseResponse.getData().getAmount())",
                   target="amount"),
           @Mapping(expression="java(null == purchaseResponse.getData() ? null :  co.za.flash.esb.electricity.mapper.shared.BillingInfoMapper.INSTANCE.toDTO(purchaseResponse.getData().getBillingInfo()))",
                   target="billingInfo"),
           @Mapping(expression="java(null == purchaseResponse.getData() ? null :  co.za.flash.esb.electricity.mapper.shared.MeterInfoMapper.INSTANCE.toDTO(purchaseResponse.getData().getMeterInfo()))",
                   target="meterInfo"),
           @Mapping(expression="java(null == purchaseResponse.getData() ? null :  co.za.flash.esb.electricity.mapper.shared.TokenMapper.INSTANCE.toDTOList(purchaseResponse.getData().getTokens()))",
                   target="tokens"),



   })
   PurchaseResponseDTO toDTO(PurchaseResponseMdl purchaseResponse);

/*   @Mappings({
           @Mapping(source = "amount", target = "arrearAmount"),
   })
   BillingInfoDTO billingInfoToDTO(BillingInfoMdl billingInfo);

   MeterInfoDTO meterInfoToDTO(MeterInfoMdl meterInfo);*/
}
