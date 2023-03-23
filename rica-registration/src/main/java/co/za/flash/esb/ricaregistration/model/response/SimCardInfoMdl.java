package co.za.flash.esb.ricaregistration.model.response;

import lombok.Data;

import java.util.List;

@Data
public class SimCardInfoMdl {
    private List<SimCardMdl> simcards;
    private List<String> iccidsNotFound;
}
