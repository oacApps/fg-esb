package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.AccountDTO;
import co.za.flash.esb.electricity.model.shared.AccountMdl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
    AccountDTO toDTO(AccountMdl object);
    AccountMdl toMdl(AccountDTO object);
}
