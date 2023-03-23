package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dto.shared.ChangeVoucherDTO;
import co.za.flash.esb.oneforyou.trader.model.shared.ChangeVoucherMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = AmountMapper.class)
public interface ChangeVoucherMapper {

    ChangeVoucherMapper INSTANCE = Mappers.getMapper(ChangeVoucherMapper.class);

    @Mappings({
            @Mapping(source ="voucherPin",  target = "pin"),
            @Mapping(source ="voucherSerial",  target = "serialNumber")
    })
    ChangeVoucherDTO map(ChangeVoucherMdl changeVoucherMdl);
}

