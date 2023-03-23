package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.BillingInfoDTO;
import co.za.flash.esb.electricity.model.shared.BillingInfoMdl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillingInfoMapper {
    BillingInfoMapper INSTANCE = Mappers.getMapper(BillingInfoMapper.class);

/*    @Mappings({
            @Mapping(source = "amount", target = "amount"),
            @Mapping(source = "arrearAmount", target = "arrearAmount"),
    })*/
    BillingInfoDTO toDTO(BillingInfoMdl billingInfo);

}
