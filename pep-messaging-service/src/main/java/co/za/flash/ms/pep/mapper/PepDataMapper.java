package co.za.flash.ms.pep.mapper;

import co.za.flash.ms.pep.entity.PepFailedRecord;
import co.za.flash.ms.pep.model.PepData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PepDataMapper {

    PepDataMapper INSTANCE = Mappers.getMapper(PepDataMapper.class);

    PepFailedRecord toEntity(PepData pepData);
    PepData toMDL(PepFailedRecord pepFailedRecord);

    List<PepFailedRecord> toEntityList(List<PepData> pepDataList);
    List<PepData> toMDLList(List<PepFailedRecord> pepFailedRecordList);
}
