package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.request.ReversalsRedemptionRequestDTO;
import co.za.flash.esb.oneforyou.trader.model.request.ReversalsRedemptionRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ReversalsRequestMapper {

    ReversalsRequestMapper INSTANCE = Mappers.getMapper(ReversalsRequestMapper.class);

    @Mappings({
            @Mapping(expression ="java(reversalsRedemptionRequestDTO.getAccountNumber())", target="user.purseAccountNumber"),
            @Mapping(target="device.channelID", constant ="OPEN_API"),
            @Mapping(target="device.appType", constant ="6"),
            @Mapping(source = "redemptionTransactionId", target = "transactionReference")
    })
    ReversalsRedemptionRequestMdl toMdl(ReversalsRedemptionRequestDTO reversalsRedemptionRequestDTO);
}
