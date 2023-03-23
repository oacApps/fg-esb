package co.za.flash.ms.georep.mapper;

import co.za.flash.ms.georep.entity.LocationsEntity;
import co.za.flash.ms.georep.entity.LocationsFailedRecordsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LocationsRecordMapper {

    LocationsRecordMapper INSTANCE = Mappers.getMapper(LocationsRecordMapper.class);

    List<LocationsFailedRecordsEntity> toFailedRecordList(List<LocationsEntity> locationsEntities);

    List<LocationsEntity> toRecordList(List<LocationsFailedRecordsEntity> locationsFailedRecordsEntities);
}
