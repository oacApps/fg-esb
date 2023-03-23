package co.za.flash.esb.avtrouting.mapper;

import co.za.flash.esb.avtrouting.model.dto.request.RedeemRequestDTO;
import co.za.flash.esb.avtrouting.model.request.RedeemRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RedeemRequestMapper {

    RedeemRequestMapper INSTANCE = Mappers.getMapper(RedeemRequestMapper.class);

    @Mappings({
            @Mapping(expression="java(null == redeem.getTransactionGuid() ? null : redeem.getTransactionGuid())", target="referenceId"),
            @Mapping(source = "voucherPin", target="pin"),
            @Mapping(expression="java(null == redeem.getAcquirer() ? null : redeem.getAcquirer().getReference())", target="userId"),
    })
    RedeemRequestMdl toMdl(RedeemRequestDTO redeem);

}


