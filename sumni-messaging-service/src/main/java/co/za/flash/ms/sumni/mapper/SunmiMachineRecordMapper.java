package co.za.flash.ms.sumni.mapper;

import co.za.flash.ms.sumni.entity.SunmiMachineFailedRecordEntity;
import co.za.flash.ms.sumni.entity.SunmiMachineViewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SunmiMachineRecordMapper {

    SunmiMachineRecordMapper INSTANCE = Mappers.getMapper(SunmiMachineRecordMapper.class);

    List<SunmiMachineFailedRecordEntity> toFailedRecordList(List<SunmiMachineViewEntity> sunmiMachineViewEntitys);

    List<SunmiMachineViewEntity> toRecordList(List<SunmiMachineFailedRecordEntity> sunmiMachineFailedRecordEntities);
}
