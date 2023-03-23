package co.za.flash.esb.ricaregistration.mapper;

import co.za.flash.esb.ricaregistration.dto.request.RegistrationsRequestDTO;
import co.za.flash.esb.ricaregistration.dto.request.SimCardInfoRequestDTO;
import co.za.flash.esb.ricaregistration.dto.response.SimCardInfoResponseDTO;
import co.za.flash.esb.ricaregistration.model.request.RegistrationsRequestMdl;
import co.za.flash.esb.ricaregistration.model.request.SimCardInfoRequestMdl;
import co.za.flash.esb.ricaregistration.model.response.SimCardInfoMdl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;

@Mapper
public interface RegistrationRequestMapper {
    RegistrationRequestMapper INSTANCE= Mappers.getMapper(RegistrationRequestMapper.class);

    RegistrationsRequestMdl toRequestMDL(RegistrationsRequestDTO registrationsRequestDTO) throws ParseException;
}
