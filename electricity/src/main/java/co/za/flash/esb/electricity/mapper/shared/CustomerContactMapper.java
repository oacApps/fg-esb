package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.CustomerContactDTO;
import co.za.flash.esb.electricity.model.shared.CustomerContactMdl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerContactMapper {
    CustomerContactMapper INSTANCE = Mappers.getMapper(CustomerContactMapper.class);
    CustomerContactDTO toDTO(CustomerContactMdl object);
    CustomerContactMdl toMdl(CustomerContactDTO object);

}
