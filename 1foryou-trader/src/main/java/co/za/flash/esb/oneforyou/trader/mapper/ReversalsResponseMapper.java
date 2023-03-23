package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.response.ReversalsRedemptionResponseDTO;
import co.za.flash.esb.oneforyou.trader.model.response.ReversalsResponseMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ReversalsResponseMapper {

    ReversalsResponseMapper INSTANCE = Mappers.getMapper(ReversalsResponseMapper.class);

    @Mappings({
            @Mapping(source ="actionCode", target="responseCode"),
            @Mapping(source ="screenMessage", target="responseMessage"),
            @Mapping(source ="transactionReference", target="transactionId"),
            @Mapping(constant = "ZAR", target="balance.currency"),
            @Mapping(source = "balance", target="balance.value"),
            @Mapping(constant = "ZAR", target="availableBalance.currency"),
            @Mapping(source = "availableBalance", target="availableBalance.value"),
    })
    ReversalsRedemptionResponseDTO toDTO(ReversalsResponseMdl reversalsResponseMdl);
}
