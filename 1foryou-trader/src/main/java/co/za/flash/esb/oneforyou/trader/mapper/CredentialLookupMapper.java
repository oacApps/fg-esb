package co.za.flash.esb.oneforyou.trader.mapper;

import co.za.flash.esb.oneforyou.trader.dblookup.CredentialLookupDTO;
import co.za.flash.esb.oneforyou.trader.dblookup.TreasuryCredentialLookupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CredentialLookupMapper {

    CredentialLookupMapper INSTANCE = Mappers.getMapper(CredentialLookupMapper.class);

    CredentialLookupDTO toDto(TreasuryCredentialLookupEntity treasuryCredentialLookupEntity);
}

