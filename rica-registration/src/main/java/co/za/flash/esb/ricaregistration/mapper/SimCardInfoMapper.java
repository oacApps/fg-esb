package co.za.flash.esb.ricaregistration.mapper;

import co.za.flash.esb.ricaregistration.dto.request.SimCardInfoRequestDTO;
import co.za.flash.esb.ricaregistration.dto.response.SimCardInfoResponseDTO;
import co.za.flash.esb.ricaregistration.model.request.SimCardInfoRequestMdl;
import co.za.flash.esb.ricaregistration.model.response.SimCardInfoMdl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface SimCardInfoMapper {

    SimCardInfoMapper INSTANCE= Mappers.getMapper(SimCardInfoMapper.class);

    SimCardInfoRequestMdl toRequestMDL(SimCardInfoRequestDTO simCardInfoRequestDTO);

    SimCardInfoResponseDTO toResponseDTO(SimCardInfoMdl simCardInfoMdl) throws ParseException;
}
