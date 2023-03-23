package co.za.flash.esb.avtrouting.mapper;

import co.za.flash.esb.avtrouting.model.dto.response.RedeemResponseDTO;
import co.za.flash.esb.avtrouting.model.response.RedeemResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RedeemResponseMapper {

    RedeemResponseMapper INSTANCE = Mappers.getMapper(RedeemResponseMapper.class);

    @Mappings({
            @Mapping(expression="java(null == redeemResponseMdl.getData() ? null : (long)(redeemResponseMdl.getData().getAmountCents()))", target="amountAuthorised.value"),
            @Mapping(defaultValue ="ZAR", target="RedeemResponseDTO.amountAuthorised.currency"),
            @Mapping(expression="java(null == redeemResponseMdl.getData() ? null : (long)(redeemResponseMdl.getData().getWallet().getAvailableBalance() * 100 ))", target="availableBalance.value"),
            /*@Mapping(defaultValue ="ZAR", target="availableBalance.currency"),*/
            @Mapping(expression="java(null == redeemResponseMdl.getData() ? null : (long)(redeemResponseMdl.getData().getWallet().getBalance() * 100 ))", target="ledgerBalance.value"),
            /*@Mapping(defaultValue ="ZAR", target="ledgerBalance.currency"),*/
            @Mapping(expression="java(null == redeemResponseMdl.getData() ? null : String.valueOf(redeemResponseMdl.getData().getSwitchId()))", target="transactionReference"),
    })
    RedeemResponseDTO toDTO(RedeemResponseMdl redeemResponseMdl);

}
