package co.za.flash.esb.electricity.mapper.shared;

import co.za.flash.esb.electricity.dto.shared.TokenDTO;
import co.za.flash.esb.electricity.model.shared.TokenMdl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TokenMapper {
    TokenMapper INSTANCE = Mappers.getMapper(TokenMapper.class);

    //List<TokenDTO> toDTOList(List<TokenMdl> object);
    List<TokenDTO> toDTOList(List<TokenMdl> tokenMdls);

    //TokenDTO toDTO(TokenMdl object);
   // TokenMdl toMdl(TokenDTO object);

}
