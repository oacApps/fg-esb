package co.za.flash.esb.oneforyou.service;

import co.za.flash.esb.oneforyou.dto.request.PurchaseRequestDTO;
import co.za.flash.esb.oneforyou.dto.request.RedeemRequestDTO;
import co.za.flash.esb.oneforyou.dto.response.PurchaseResponseDTO;
import org.springframework.http.HttpHeaders;

public interface OneForUServiceInf {

    PurchaseResponseDTO purchaseService(HttpHeaders headers,
                                        PurchaseRequestDTO purchaseRequestDTO,
                                        String organization,
                                        String keyType,
                                        String backEndUrl,
                                        String seqUrl);

    Object redeemService(HttpHeaders httpHeaders,
                         RedeemRequestDTO redeemRequestDTO,
                         String organization,
                         String keyType,
                         String backEndUrl,
                         String seqUrl);
}
