package co.za.flash.esb.oneforyou.mapper.shared;

import co.za.flash.esb.oneforyou.dto.shared.ChangeVoucherDTO;
import co.za.flash.esb.oneforyou.model.shared.ChangeVoucherMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ChangeVoucherMapper {
    ChangeVoucherMapper INSTANCE = Mappers.getMapper(ChangeVoucherMapper.class);
    @Mappings({
            @Mapping(source = "amount", target = "amountCents"),
    })
    ChangeVoucherMdl toMdl(ChangeVoucherDTO object);

    @Mappings({
            @Mapping(source = "amountCents", target = "amount"),
    })
    ChangeVoucherDTO toDTO(ChangeVoucherMdl object);
}
