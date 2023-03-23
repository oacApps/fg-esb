package co.za.flash.esb.flashvoucher.mapper;

import co.za.flash.esb.flashvoucher.dto.RequestDTO;
import co.za.flash.esb.flashvoucher.model.RequestMDL;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);
    //target = "acquirer.account.balance"
    @Mappings({
            @Mapping(source = "accountNumber", target = "user.purseAccountNumber"),
            @Mapping(expression="java(requestDTO.getAmount().getCurrency())", target = "currency"),
            @Mapping(expression="java(String.valueOf(requestDTO.getAmount().getValue()))", target = "amountRequested"),

    })
    RequestMDL toMdl (RequestDTO requestDTO);
}
