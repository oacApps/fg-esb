package co.za.flash.esb.electricity.mapper;

import co.za.flash.esb.electricity.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.electricity.model.request.PurchaseRequestMdl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PurchaseRequestMapper {
    PurchaseRequestMapper INSTANCE = Mappers.getMapper(PurchaseRequestMapper.class);
    PurchaseRequestMdl toMdl(PurchaseRequestDTO object);
}
