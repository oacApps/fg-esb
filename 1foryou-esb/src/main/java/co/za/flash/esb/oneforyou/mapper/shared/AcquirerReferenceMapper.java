package co.za.flash.esb.oneforyou.mapper.shared;

import co.za.flash.esb.oneforyou.dto.shared.AcquirerDTO;
import co.za.flash.esb.oneforyou.model.shared.AcquirerReferenceMdl;
import org.mapstruct.factory.Mappers;

public interface AcquirerReferenceMapper {
    AcquirerReferenceMapper INSTANCE = Mappers.getMapper(AcquirerReferenceMapper.class);
    AcquirerReferenceMdl toMdl(AcquirerDTO object);
}
