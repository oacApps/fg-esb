package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.VoucherDTO;
import co.za.flash.esb.electricity.model.shared.VoucherMdl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VoucherMapper {
    VoucherMapper INSTANCE = Mappers.getMapper(VoucherMapper.class);
    VoucherDTO toDTO(VoucherMdl object);
    VoucherMdl toMdl(VoucherDTO object);
}
